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
  new chipyard.harness.

  new chipyard.config.WithI2C(address = 0x10003000) ++
  //new chipyard.config.WithSPI(address = 0x10002000) ++
  //new chipyard.config.WithSPIFlash ++
  new chipyard.config.WithGPIO(address = 0x10001000, width = gpio) ++
  new chipyard.config.WithUART(address = 0x10000000) ++
  new freechips.rocketchip.subsystem.WithDefaultMemPort
)

class CustomSoC extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new PeripheralConfig(8) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++
  new chipyard.config.WithNoUART ++
  new testchipip.soc.WithNoScratchpads ++
  new freechips.rocketchip.rocket.WithNMedCores(4) ++
  new chipyard.config.AbstractConfig
)

class CustomNoC extends Config(
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
      //topology = BidirectionalTorus1D(6),
      channelParamGen = (a, b) => UserChannelParams(Seq.fill(10) { UserVirtualChannelParams(4) }),
      routingRelation = NonblockingVirtualSubnetworksRouting(Mesh2DDimensionOrderedRouting(), 5, 2))
  )) ++
  new CustomSoC ++
  new chipyard.config.AbstractConfig
)