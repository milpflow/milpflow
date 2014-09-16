#!/bin/bash
#Ex.: serverTCP.sh result_hA_hB_tcp_5m.txt

iperf -s -i 5 > $1 