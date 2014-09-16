#!/bin/bash
#Ex.: server.sh result_hA_hB_udp_5m.txt

iperf -s -u -i 5 > $1 