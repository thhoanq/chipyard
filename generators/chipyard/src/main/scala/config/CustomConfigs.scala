package chipyard

import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem.{SBUS, MBUS}

import constellation.channel._
import constellation.routing._
import constellation.router._
import constellation.topology._
import constellation.noc._

import scala.collection.immutable.ListMap

class PeripheralConfig(gpio: Int = 8) extends Config(
  new chipyard.harness.WithSPITiedOff ++
  new chipyard.harness.WithI2CTiedOff ++

  new chipyard.cipher.WithBLAKE2S(address = 0x10007000) ++
  new chipyard.cipher.WithKLEIN(address = 0x10006000) ++
  new chipyard.config.WithI2C(address = 0x10005000) ++
  new chipyard.config.WithSPI(address = 0x10004000) ++
  new chipyard.config.WithSPI(address = 0x10003000) ++
  new chipyard.config.WithGPIO(address = 0x10002000, width = gpio) ++
  new chipyard.config.WithUART(address = 0x10001000) ++
  //new chipyard.config.WithUART(address = 0x10000000) ++
  new freechips.rocketchip.subsystem.WithDefaultMemPort
)

// DOC include start: GCDTLBlackBoxRocketConfig
class GCDTLBlackBoxRocketConfig extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new chipyard.cipher.WithBLAKE2S(address = 0x10007000) ++
  new chipyard.cipher.WithKLEIN(address = 0x10006000) ++
  new chipyard.cipher.WithROM ++
  new chipyard.example.WithGCD(useAXI4=false, useBlackBox=true) ++            // Use GCD blackboxed verilog, connect Tilelink
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)
// DOC include end: GCDTLBlackBoxRocketConfig

class TestSoC extends Config(
  new freechips.rocketchip.rocket.WithNCustomCores(1, withFPU = false) ++
  new freechips.rocketchip.rocket.WithNCustomCores(2, withFPU = true, lengthFPU = 32) ++
  new freechips.rocketchip.rocket.WithNCustomCores(1, withFPU = true, lengthFPU = 64) ++
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  //new freechips.rocketchip.subsystem.WithInclusiveCache(nWays=4, capacityKB=128) ++
  new chipyard.config.AbstractConfig
)

class CustomSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(8) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNHugeCores(4) ++
  new chipyard.config.AbstractConfig
)

class SingleCoreSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
    new PeripheralConfig(8) ++
    new freechips.rocketchip.subsystem.WithNoMemPort ++
    new chipyard.config.WithNoUART ++
    new testchipip.soc.WithNoScratchpads ++
    new freechips.rocketchip.rocket.WithNHugeCores(1) ++
    new chipyard.config.AbstractConfig
)

class DualCoreSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(8) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNMedCores(2) ++
  new chipyard.config.AbstractConfig
)

class DualCoreNoC extends Config(
  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
    constellation.protocol.DiplomaticNetworkNodeMapping(
      inNodeMapping = ListMap(
        "Core 0" -> 0, "Core 1" -> 1,
        "serial_tl" -> 2),
      outNodeMapping = ListMap(
        "system[0]" -> 3,
        "pbus" -> 2)),
    nocParams = NoCParams(
      topology = Mesh2D(nX = 2, nY = 2),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(Mesh2DDimensionOrderedRouting(), 5, 2))
  )) ++
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(8) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNMedCores(2) ++
  new chipyard.config.AbstractConfig
)

class CustomNoC extends Config(
//  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
//    constellation.protocol.DiplomaticNetworkNodeMapping(
//      inNodeMapping = ListMap(
//        "Core 0" -> 0, "Core 1" -> 1,
//        "Core 2" -> 2, "Core 3" -> 3,
//        "Core 4" -> 5, "Core 5" -> 6,
//        "Core 6" -> 7, "Core 7" -> 8,
//        "serial_tl" -> 4),
//      outNodeMapping = ListMap(
//        "system[0]" -> 4,
//        "pbus" -> 4)),
//    nocParams = NoCParams(
//      topology = Mesh2D(nX = 3, nY = 3),
//      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
//      routingRelation = NonblockingVirtualSubnetworksRouting(Mesh2DDimensionOrderedRouting(), 5, 2))
//  )) ++
  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
    constellation.protocol.DiplomaticNetworkNodeMapping(
      inNodeMapping = ListMap(
        "Core 0" -> 1, "Core 1" -> 2,
        "Core 2" -> 4, "Core 3" -> 5,
        "serial_tl" -> 0),
      outNodeMapping = ListMap(
        "system[0]" -> 3,
        "pbus" -> 0)),
    nocParams = NoCParams(
      topology = Mesh2D(nX = 3, nY = 2),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(Mesh2DDimensionOrderedRouting(), 5, 2))
  )) ++
  new CustomSoC ++
  new chipyard.config.AbstractConfig
)