// See LICENSE for license details.
package chipyard.fpga.arty100t

import sys.process._

import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import org.chipsalliance.diplomacy._
import org.chipsalliance.diplomacy.lazymodule._
import freechips.rocketchip.system._
import freechips.rocketchip.tile._
import sifive.blocks.devices.uart._
import sifive.fpgashells.shell.DesignKey
import testchipip.serdes.SerialTLKey
import chipyard.BuildSystem
import constellation.channel.{UserChannelParams, UserVirtualChannelParams}
import constellation.noc.NoCParams
import constellation.routing._
import constellation.topology.Mesh2D

import scala.collection.immutable.ListMap


// BootROOM Configuration
class WithSimpleBootROM extends Config((site, here, up) => {
  case BootROMLocated(x) => up(BootROMLocated(x), site).map{ p =>
    val freqMHz = (site(SystemBusKey).dtsFrequency.get / (1000 * 1000)).toLong
    // Make sure that the bootrom is always rebuilt
    val clean = s"make -C fpga/src/main/resources/bootROM/basic clean"
    require (clean.! == 0, "Failed to clean")
    // Build the bootrom
    val make = s"make -C fpga/src/main/resources/bootROM/basic XLEN=64 PBUS_CLK=${freqMHz}"
    require (make.! == 0, "Failed to build bootrom")
    p.copy(hang = 0x10000, contentFileName = s"./fpga/src/main/resources/bootROM/basic/build/sdboot.bin")
  }
})

// don't use FPGAShell's DesignKey
class WithNoDesignKey extends Config((site, here, up) => {
  case DesignKey => (p: Parameters) => new SimpleLazyRawModule()(p)
})

// By default, this uses the on-board USB-UART for the TSI-over-UART link
// The PMODUART HarnessBinder maps the actual UART device to JD pin
class WithArty100TTweaks(freqMHz: Double = 50) extends Config(
//  new WithArty100TGPIO ++
  new WithArty100TPMODUARTs ++
  new WithArty100TUARTTSI ++
  new WithArty100TDDRTL ++
  new WithArty100TJTAG ++
  new WithNoDesignKey ++
  new testchipip.tsi.WithUARTTSIClient ++
  new chipyard.harness.WithSerialTLTiedOff ++
  new chipyard.harness.WithHarnessBinderClockFreqMHz(freqMHz) ++
  new chipyard.config.WithUniformBusFrequencies(freqMHz) ++
  new chipyard.harness.WithAllClocksFromHarnessClockInstantiator ++
  new chipyard.clocking.WithPassthroughClockGenerator ++
  new WithSimpleBootROM ++
  new chipyard.config.WithTLBackingMemory ++ // FPGA-shells converts the AXI to TL for us
  new freechips.rocketchip.subsystem.WithExtMemSize(BigInt(256) << 20) ++ // 256mb on ARTY
  new freechips.rocketchip.subsystem.WithoutTLMonitors)

class RocketArty100TConfig extends Config(
  new WithArty100TTweaks ++
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.RocketConfig)

class NoCoresArty100TConfig extends Config(
  new WithArty100TTweaks ++
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.NoCoresConfig)

// This will fail to close timing above 50 MHz
class BringupArty100TConfig extends Config(
  new WithArty100TSerialTLToGPIO ++
  new WithArty100TTweaks(freqMHz = 50) ++
  new testchipip.serdes.WithSerialTLPHYParams(testchipip.serdes.InternalSyncSerialPhyParams(freqMHz=50)) ++
  new chipyard.ChipBringupHostConfig)

// single 64-bit Rocket
class FPGACustomSoC extends Config(
  new WithArty100TTweaks ++
  // Config peripheral
  new chipyard.harness.WithI2CTiedOff ++
  new chipyard.iobinders.WithGPIOPunchthrough ++
  new chipyard.harness.WithSPITiedOff ++

  new chipyard.config.WithI2C(address = 0x10005000) ++
  new chipyard.config.WithSPI(address = 0x10004000) ++
  new chipyard.config.WithSPI(address = 0x10003000) ++
  new chipyard.config.WithGPIO(address = 0x10002000, width = 8) ++
  new chipyard.config.WithUART(address = 0x10001000) ++
  new chipyard.config.WithUART(address = 0x10000000) ++
  // Base config
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.config.WithNoUART ++
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig
)
