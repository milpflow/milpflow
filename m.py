#!/usr/bin/env python

from mininet.topo import Topo
from mininet.cli import CLI
from mininet.link import TCLink
from mininet.node import CPULimitedHost
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.term import makeTerm

if '__main__' == __name__:

    net = Mininet(link=TCLink,host=CPULimitedHost,controller=RemoteController)

    c0 = net.addController('c0')
    s1 = net.addSwitch('s1')
    s2 = net.addSwitch('s2')
    s3 = net.addSwitch('s3')
    s4 = net.addSwitch('s4')
    s5 = net.addSwitch('s5')
    s6 = net.addSwitch('s6')
    s7 = net.addSwitch('s7')
    s8 = net.addSwitch('s8')
    s9 = net.addSwitch('s9')
    s10 = net.addSwitch('s10')
    s11 = net.addSwitch('s11')
    s12 = net.addSwitch('s12')
    s13 = net.addSwitch('s13')
    s14 = net.addSwitch('s14')
    s15 = net.addSwitch('s15')
    s16 = net.addSwitch('s16')
    s17 = net.addSwitch('s17')
    h18 = net.addHost('h18', cpu=.01)
    h19 = net.addHost('h19', cpu=.01)
    h20 = net.addHost('h20', cpu=.01)
    h21 = net.addHost('h21', cpu=.01)
    h22 = net.addHost('h22', cpu=.01)
    h23 = net.addHost('h23', cpu=.01)
    h24 = net.addHost('h24', cpu=.01)
    h25 = net.addHost('h25', cpu=.01)
    h26 = net.addHost('h26', cpu=.01)
    h27 = net.addHost('h27', cpu=.01)
    h28 = net.addHost('h28', cpu=.01)
    h29 = net.addHost('h29', cpu=.01)

    TCLink(s2, s1, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s3, s1, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s4, s1, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s5, s1, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s2, s3, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s3, s4, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s4, s5, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s6, s2, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s6, s3, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s7, s4, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s7, s5, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s8, s2, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s8, s3, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s9, s4, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s9, s5, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s10, s2, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s10, s3, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s11, s4, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s11, s5, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s6, s7, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s7, s8, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s8, s9, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s9, s10, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s10, s11, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s12, s6, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s12, s7, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s13, s6, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s13, s7, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s14, s8, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s14, s9, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s15, s8, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s15, s9, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s16, s10, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s16, s11, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s17, s10, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(s17, s11, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h18, s12, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h19, s12, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h20, s13, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h21, s13, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h22, s14, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h23, s14, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h24, s15, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h25, s15, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h26, s16, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h27, s16, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h28, s17, bw=10, delay='10ms', loss=0, use_htb=False)
    TCLink(h29, s17, bw=10, delay='10ms', loss=0, use_htb=False)

    net.build()
    c0.start()
    s1.start([c0])
    s2.start([c0])
    s3.start([c0])
    s4.start([c0])
    s5.start([c0])
    s6.start([c0])
    s7.start([c0])
    s8.start([c0])
    s9.start([c0])
    s10.start([c0])
    s11.start([c0])
    s12.start([c0])
    s13.start([c0])
    s14.start([c0])
    s15.start([c0])
    s16.start([c0])
    s17.start([c0])

    CLI(net)
    net.stop()
