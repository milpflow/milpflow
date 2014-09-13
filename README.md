Milpflow 
========

MILPFlow (Mixed Integer Linear Programming with OpenFlow)

Hi! This is a simple tutorial for you start using MILPFlow.


TCP Evaluation Performance:
==========================

Start the TCP clients:

mininet> h18 xterm&
./clientTCP.sh 10.0.0.12

mininet> h19 xterm&
./clientTCP.sh 10.0.0.11

mininet> h20 xterm&
./clientTCP.sh 10.0.0.10

mininet> h21 xterm&
./clientTCP.sh 10.0.0.9

mininet> h22 xterm&
./clientTCP.sh 10.0.0.8

mininet> h23 xterm&
./clientTCP.sh 10.0.0.7


Start the TCP servers:

mininet> h29 xterm&
./serverTCP.sh result_h18_h29_tcp_5m.txt

mininet> h28 xterm&
./serverTCP.sh result_h19_h28_tcp_5m.txt

mininet> h27 xterm&
./serverTCP.sh result_h20_h27_tcp_5m.txt

mininet> h26 xterm&
./serverTCP.sh result_h21_h26_tcp_5m.txt

mininet> h25 xterm&
./serverTCP.sh result_h22_h25_tcp_5m.txt

mininet> h24 xterm&
./serverTCP.sh result_h23_h24_tcp_5m.txt
