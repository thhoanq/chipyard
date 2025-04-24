package chipyard

import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem.{MBUS, SBUS}
import constellation.channel._
import constellation.routing._
import constellation.router._
import constellation.topology._
import constellation.noc._

import scala.collection.immutable.ListMap

class PeripheralConfig(gpio: Int = 8) extends Config(
//  new chipyard.cipher.WithMyTimer(address = 0x1000E000) ++
  new chipyard.cipher.WithAES(address = 0x10008000) ++
  new chipyard.cipher.WithSHA3(address = 0x10007000) ++
//  new chipyard.cipher.WithPrince(address = 0x1000B000) ++
//  new chipyard.cipher.WithBLAKE2S(address = 0x1000A000) ++
//  new chipyard.cipher.WithDMPresent(address = 0x10009000) ++
//  new chipyard.cipher.WithPresent(address = 0x10008000) ++
  new chipyard.cipher.WithChaCha(address = 0x10006000)
//  new chipyard.cipher.WithKLEIN(address = 0x10006000) ++
)

// DOC include start: GCDTLBlackBoxRocketConfig
class GCDTLBlackBoxRocketConfig extends Config(
  new chipyard.cipher.WithMyTimer(address = 0x1000E000) ++
//    new chipyard.cipher.WithAES(address = 0x1000D000) ++
//    new chipyard.cipher.WithSHA3(address = 0x1000C000) ++
//    new chipyard.cipher.WithPrince(address = 0x1000B000) ++
//    new chipyard.cipher.WithBLAKE2S(address = 0x1000A000) ++
//    new chipyard.cipher.WithDMPresent(address = 0x10009000) ++
//    new chipyard.cipher.WithPresent(address = 0x10008000) ++
//    new chipyard.cipher.WithChaCha(address = 0x10007000) ++
//    new chipyard.cipher.WithKLEIN(address = 0x10006000) ++
    new chipyard.cipher.WithASCON(address = 0x10006000) ++
    new freechips.rocketchip.subsystem.WithoutTLMonitors ++
    new freechips.rocketchip.rocket.WithNHugeCores(1) ++
    new chipyard.config.AbstractConfig)
// DOC include end: GCDTLBlackBoxRocketConfig

class CustomSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(8) ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
//  new freechips.rocketchip.rocket.WithNHugeCores(4) ++
    new freechips.rocketchip.rocket.WithNCustomCores(1, withFPU = false) ++
    new freechips.rocketchip.rocket.WithNCustomCores(2, withFPU = true, lengthFPU = 32) ++
    new freechips.rocketchip.rocket.WithNCustomCores(1, withFPU = true, lengthFPU = 64) ++
  new chipyard.config.AbstractConfig
)

class SingleCoreSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(1) ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNMedCores(1) ++
  new chipyard.config.AbstractConfig
)

// DOC include start TestConfigSoC
class QuadCoreSoC extends Config(
  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
    constellation.protocol.DiplomaticNetworkNodeMapping(
      inNodeMapping = ListMap(
        "Core 0" -> 0, "Core 1" -> 1,
        "Core 2" -> 3, "Core 3" -> 4,
        "serial_tl" -> 2),
      outNodeMapping = ListMap(
        "system[0]" -> 2,
        "pbus" -> 2)),
    nocParams = NoCParams(
      topology = BidirectionalTorus1D(5),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(BidirectionalTorus1DShortestRouting(), 5, 2))
  )) ++
  new freechips.rocketchip.subsystem.WithDefaultMemPort ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++
  new freechips.rocketchip.rocket.WithRV32 ++
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNSmallCores(4) ++
  new chipyard.config.AbstractConfig
)
// DOC include end TestConfigSoC

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

// DOC include start: QuadCoreRing
class QuadCoreRing extends Config(
  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
    constellation.protocol.DiplomaticNetworkNodeMapping(
      inNodeMapping = ListMap(
        "Core 0" -> 0, "Core 1" -> 1,
        "Core 2" -> 2, "Core 3" -> 3,
        "debug[0]" -> 4),
      outNodeMapping = ListMap(
        "system[0]" -> 5,
        "pbus" -> 4)),
    nocParams = NoCParams(
      topology = BidirectionalTorus1D(6),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(BidirectionalTorus1DShortestRouting(), 5, 2))
  )) ++
  new CustomSoC ++
  new chipyard.config.AbstractConfig
)
// DOC include end: QuadCoreRing

// DOC include start: QuadCoreMesh
class QuadCoreMesh extends Config(
  new constellation.soc.WithSbusNoC(constellation.protocol.SimpleTLNoCParams(
    constellation.protocol.DiplomaticNetworkNodeMapping(
      inNodeMapping = ListMap(
        "Core 0" -> 0, "Core 1" -> 1,
        "Core 2" -> 2, "Core 3" -> 3,
        "debug[0]" -> 5),
      outNodeMapping = ListMap(
        "system[0]" -> 4,
        "pbus" -> 5)),
    nocParams = NoCParams(
      topology = Mesh2D(nX = 3, nY = 2),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) {UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(Mesh2DDimensionOrderedRouting(), 5, 2))
  )) ++
  new CustomSoC ++
  new chipyard.config.AbstractConfig
)
// DOC include end: QuadCoreMesh