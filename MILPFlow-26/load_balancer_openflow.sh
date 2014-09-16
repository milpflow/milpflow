#DP0=switch1=s5
#Handle ARP messages
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=1
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.2 apply:output=2
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:output=4
#Handle ICMP messages:
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.2 apply:output=2
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.3 apply:output=4
#Direct incoming packets of all type which arrive at port 4 to port 1:
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add in_port=4 apply:output=1
#Group table creation and configuration:
dpctl tcp:127.0.0.1:6634 group-mod cmd=add,type=1,group=1 weight=7,port=1,group=all output=4 weight=2,port=1,group=all output=3 weight=1,port=1,group=all output=2
#Only after group creation I can modify flows from groups on host 10.0.0.4
#Handle ARP messages
dpctl tcp:127.0.0.1:6634 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.4 apply:group=1
#Handle ICMP messages
dpctl tcp:127.0.0.1:6634 flow-mod cmd=add,table=0 eth_type=0x800,ip_dst=10.0.0.4 apply:group=1
#DP1=switch2=s6
#Direct incoming packets of all type which arrive at port 1 to port 2:
dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=1 apply:output=2
#DP2=Switch3=s7
#Direct incoming packets of all type which arrive at port 1 to port 2:
dpctl tcp:127.0.0.1:6636 flow-mod table=0,cmd=add in_port=1 apply:output=2
#DP3=Switch4=s8
#Direct incoming packets of all type which arrive at port 1 to port 2:
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=2
#DP4=Switch5=s9
#Handle ARP messages:
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=1
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.2 apply:output=4
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:output=5
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.4 apply:output=6
#Handle ICMP messages:
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.1 apply:output=1
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.2 apply:output=4
dpctl tcp:127.0.0.1:6638 flow-mod cmd=add,table=0 eth_type=0x800,ip_dst=10.0.0.3 apply:output=5
dpctl tcp:127.0.0.1:6638 flow-mod cmd=add,table=0 eth_type=0x800,ip_dst=10.0.0.4 apply:output=6
