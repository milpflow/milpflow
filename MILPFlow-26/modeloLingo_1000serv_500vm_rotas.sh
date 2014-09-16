# F_6_4
# s4
# (<--) ARP
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=3
# (<--) ICMP and all others:
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
#Destination: 
# (-->) ARP 
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:output=1
# (-->) ICMP and all others 
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.3 apply:output=1

#F_4_2
#s2: ARP only in switches with direct connection with hosts (not here!)
# (<--) ICMP and all others 
dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3
#(-->) ICMP and all others 
dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4

#F_2_5
#s5: Handle in destination is different of handle in source (need to mount packages)
#(<--) ARP#
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=1
#(<--) ICMP and all others
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.1 apply:output=1
# Last switch from sequence
#(-->) ARP#
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:output=3
#(-->) ICMP and all others
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.3 apply:output=3
