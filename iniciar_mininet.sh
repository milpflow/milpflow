#!/bin/bash
# Use this script to initiate Mininet with Ryu and DPCTL 1.0

#apt-get update
#apt-get install -y git automake autoconf gcc uml-utilities libtool build-essential git pkg-config linux-headers-`uname -r`
#wget http://openvswitch.org/releases/openvswitch-2.1.3.tar.gz
#tar zxvf openvswitch-2.1.3.tar.gz
#cd openvswitch-2.1.3
#./boot.sh
#./configure --with-linux=/lib/modules/`uname -r`/build
#make && make install && make modules_install
modprobe openvswitch
#insmod ./datapath/linux/openvswitch.ko
mkdir -p /usr/local/etc/openvswitch
ovsdb-tool create /usr/local/etc/openvswitch/conf.db \ 	vswitchd/vswitch.ovsschema
ovsdb-server -v --remote=punix:/usr/local/var/run/openvswitch/db.sock \
  --remote=db:Open_vSwitch,Open_vSwitch,manager_options \
  --private-key=db:Open_vSwitch,SSL,private_key \
  --certificate=db:Open_vSwitch,SSL,certificate \
  --pidfile --detach 
ovs-vsctl --no-wait init
ovs-vswitchd --pidfile --detach
ovs-vsctl show
