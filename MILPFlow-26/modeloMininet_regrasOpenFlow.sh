
#fattree minima: divide para ir, e divide para voltar

from mininet.topo import Topo

class modeloMininet_1000serv_500vm( Topo ):

	def __init__( self ):

		Topo.__init__( self )
		s1 = self.addSwitch('s1')
		s2 = self.addSwitch('s2')
		s3 = self.addSwitch('s3')
		s4 = self.addSwitch('s4')
		s5 = self.addSwitch('s5')
		h6 = self.addHost('h6')
		h7 = self.addHost('h7')
		h8 = self.addHost('h8')
		h9 = self.addHost('h9')


		self.addLink(s2, s1)
		self.addLink(s3, s1)
		self.addLink(s2, s3)
		self.addLink(s4, s2)
		self.addLink(s5, s2)
		self.addLink(s4, s3)
		self.addLink(s5, s3)
		self.addLink(h6, s4)
		self.addLink(h7, s4)
		self.addLink(h8, s5)
		self.addLink(h9, s5)

topos = {
'modeloMininet_1000serv_500vm': (lambda: modeloMininet_1000serv_500vm())
}



#s4
#Handle ARP messages
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:output=3
#Direct incoming packets of all type which arrive
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=1 apply:output=3
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add in_port=2 apply:output=3
#Group table creation and configuration:
dpctl tcp:127.0.0.1:6637 group-mod cmd=add,type=1,group=1 \
    weight=2,port=3,group=all output=1 \
    weight=1,port=3,group=all output=2
#Only after group creation I can modify flows from groups on host 10.0.0.4
#Handle ARP messages
dpctl tcp:127.0.0.1:6637 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:group=1
#Handle ICMP messages
dpctl tcp:127.0.0.1:6637 flow-mod cmd=add,table=0 eth_type=0x800,ip_dst=10.0.0.3 apply:group=1

#s2
#Direct incoming packets of all type which arrive at port 1 to port 2:
dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=3 apply:output=4
dpctl tcp:127.0.0.1:6635 flow-mod table=0,cmd=add in_port=4 apply:output=3
#s3
#Direct incoming packets of all type which arrive at port 1 to port 2:
dpctl tcp:127.0.0.1:6636 flow-mod table=0,cmd=add in_port=3 apply:output=4
dpctl tcp:127.0.0.1:6636 flow-mod table=0,cmd=add in_port=4 apply:output=3
#s5
#Handle ARP messages:
dpctl tcp:127.0.0.1:6638 group-mod cmd=add,type=1,group=2 \
    weight=2,port=3,group=all output=1 \
    weight=1,port=3,group=all output=2
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.1 apply:group=2
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x806,arp_tpa=10.0.0.3 apply:output=3
#Handle ICMP messages:
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.1 apply:group=2
dpctl tcp:127.0.0.1:6638 flow-mod table=0,cmd=add eth_type=0x800,ip_dst=10.0.0.3 apply:output=3
