#!/bin/bash

killall ovsdb-server
killall ovs-vswitchd
service openvswitch-switch restart
mn
