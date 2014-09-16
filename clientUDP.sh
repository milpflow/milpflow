#!/bin/bash
#Ex.: clientUDP.sh 10.0.0.12 5m

x=1
while [ $x -le 30 ] 
do
    echo $x
    iperf -c $1 -u -b $2
    sleep 10
    x=$(( $x +1 ))
done