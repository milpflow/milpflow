#!/bin/bash
#Ex.: clientTCP.sh 10.0.0.12 

x=1
while [ $x -le 30 ] 
do
    echo $x
    iperf -c $1
    sleep 10
    x=$(( $x +1 ))
done