# CATS
[Cable Automated Test Solution (CATS)](https://github.com/Comcast/cats/) is a pre-integrated software bundle that provides a common framework for validating customer-premises equipment (CPE) for functional testing and stability tests. The CATS automation framework can be used by the broadcast community for improving the amount, type and quality of auto-mated testing. CATS focuses on manual and automated Set top usage through IR and Power Control used for video inspection.

## Benefits
  * Reducing the Length of Test & Development Cycles
  * Shorten Time to Market
  * Improve Overall Product Quality

# Build Instructions

# Reference Hardware

For a fully functional CATS installation the following devices are needed:

1. IR emitter controller a.k.a. IR blaster
2. Power/Reboot switch - network controllable
3. IP Video Encoder
4. Network switch
5. PC/Server or VM (CentOS6/RHEL6 compatible)

Only one device in each category is needed.

# Supported Devices

## IR Emitter Controller
| Function | Vendor | Model | No of Ports | Approx. Street Price | Product Link | Comments |
| -------- | ------ | ----- | ----------- | -------------------- | ------------ | -------- |
|  IR      | Global Cache | iTach IP2IR     | 3  | ~$100 | http://www.globalcache.com/products/itach/ip2ir-pspecs/? | small footprint, can fit 6 onto a 1U rack shelf |
|  IR      | Global Cache | iTach IP2IR(-P) | 3  | ~$130 | http://www.globalcache.com/products/itach/ip2ir-pspecs/? | supports PoE (802.3af) |
|  IR      | Global Cache | GC-100-18R      | 6  | ~$250 | http://www.globalcache.com/products/itach/ip2ir-pspecs/ | rack version with six ports |

## Power/Reboot Switch
| Function | Vendor | Model | No of Ports | Approx. Street Price | Product Link | Comments |
| ---------| ------ | ----- | ----------- | -------------------- | ------------ | -------- |
|  Power    | WTI          | NPS-8HS20-1     | 8  | $760  | http://www.wti.com/p-183-nps-8hs20-1-network-power-switch-pdu-20a-120v-85-15r.aspx | Expensive |
|  Power    | Synaccess    | NP-0801DT       | 8  | $350  | http://www.synaccess-net.com/remote-power.php/1/3 | Cheaper |

## IP Video Encoder
| Function | Vendor | Model | No of Ports | Approx. Street Price | Product Link | Comments |
| ---------| ------ | ----- | ----------- | -------------------- | ------------ | -------- |
|  Video encoder | Axis    | P7214           | 4  | $700  | http://www.axis.com/products/cam_p7214/ | supports PoE(802.3af) |
|  Video encoder | Axis    | P7216           | 16 | $1700 | http://www.axis.com/products/cam_p7216/ | newest model |

## Network Switch
Any standard Ethernet network switch or router will work. The only assumption CATS makes about the network:
* The CATS server can communicate with all the devices
* The users can access the IP video encoder and the CATS service.

To reduce the amount of cabling it is preferential to use a PoE-capable (IEEE 802.3af) switch as some of the IR emitter controllers and some of the IP video encoders can be powered via PoE - reducing the number of power bricks in the whole setup.

## PC/Server
CATS is currently only supported on CentOS 6 (and RHEL 6). The PC/Server hardware needs to be therefore supported by one of these operating systems.

