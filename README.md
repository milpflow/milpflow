Milpflow 
========

MILPFlow (Mixed Integer Linear Programming with OpenFlow)

Hi! This is a simple tutorial for you start using MILPFlow.


MILPFlow Deployment of Paths:
----------------------------

* Step 1: Solve the MILP model of data traffic:

```
# cd /usr/local/src/workspace/MILPFlow-26
# ./executar_linha_comando.sh
```

* Step 2: Start the automatically generated topology for Mininet:

```
# ./modeloMininet_1000serv_500vm_ryu.py
```

* Step 3: Start the Ryu REST controller:

```
# cd ryu; ryu-manage --verbose ryu/app/ofctl_rest.py
```

* Step 4: Deploy the paths :

```
# chmod +x modeloLingo_1000serv_500vm_regrasOpenFlowRyu.sh
# ./modeloLingo_1000serv_500vm_regrasOpenFlowRyu.sh
(Note: if some rule was not deployed (HTTP 404 instead HTTP 200), restart 'ofctl_rest.py', and run this step again).
```

* Step 5: Try the connectivity between the hosts:
(Note: only the path between the hosts solved by MILPFlow will be deployed).


```
mininet> h18 ping h29
``` 


TCP Performance Evaluation:
--------------------------

More precise results are acquired when you run the host-host TCP traffic evaluation all at the same time. If the MILP model was solved with MILPFlow, all the data traffic should be run without losses of packets:

Start the TCP clients:

mininet> h18 xterm&
#./clientTCP.sh 10.0.0.12

mininet> h19 xterm&
#./clientTCP.sh 10.0.0.11

mininet> h20 xterm&
#./clientTCP.sh 10.0.0.10

mininet> h21 xterm&
#./clientTCP.sh 10.0.0.9

mininet> h22 xterm&
#./clientTCP.sh 10.0.0.8

mininet> h23 xterm&
#./clientTCP.sh 10.0.0.7


Start the TCP servers:

mininet> h29 xterm&
#./serverTCP.sh result_h18_h29_tcp_5m.txt

mininet> h28 xterm&
#./serverTCP.sh result_h19_h28_tcp_5m.txt

mininet> h27 xterm&
#./serverTCP.sh result_h20_h27_tcp_5m.txt

mininet> h26 xterm&
#./serverTCP.sh result_h21_h26_tcp_5m.txt

mininet> h25 xterm&
#./serverTCP.sh result_h22_h25_tcp_5m.txt

mininet> h24 xterm&
#./serverTCP.sh result_h23_h24_tcp_5m.txt

