Milpflow 
========

MILPFlow (Mixed Integer Linear Programming with OpenFlow)

Hi! This is a simple tutorial for you start using MILPFlow.

Virtual Machine 1: DPCTL 1.3
============================

Our approach with MILPFlow is use the REST API of Ryu OpenFlow controller to set our data paths. However, STP algorithm of Ryu is not working properly with dpctl 1.3, so we provide two VM with similar configurations.

MILPFlow also draw with GraphViz the data paths obtained in the topology. 
After run MILPFlow, the graphics are in:
```
# ls ~/modeloGraphViz*.pdf
```

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

* Step 4: Deploy the paths:

(Note: if some rule was not deployed (when you see HTTP 404 in 'ofctl_rest.py', instead HTTP 200), restart 'ofctl_rest.py', and run this step again).

```
# chmod +x modeloLingo_1000serv_500vm_regrasOpenFlowRyu.sh
# ./modeloLingo_1000serv_500vm_regrasOpenFlowRyu.sh
```

* Step 5: Try the connectivity between the hosts:
(Note: only the paths between the hosts solved by MILPFlow will be available).


```
mininet> h18 ping h29
``` 


TCP and UDP Performance Evaluation:
--------------------------

More precise results are acquired when you run the host-host TCP or UDP measurements in all hosts at the same time. If the MILP model was solved with MILPFlow, then the whole data traffic should be transfered without losses of packets between the hosts.

We provide two automatized scripts to run 30 samples in each host:

* To run TCP evaluation use: *clientTCP.sh* and *serverTCP.sh*

* To run UDP evaluation use: *clientUDP.sh* and *serverUDP.sh*


Example of TCP evaluation:
-------------------------

* Start your TCP servers in each host:

```
mininet> h29 xterm&
#./serverTCP.sh result_h18_h29_tcp_5m.txt
```

```
mininet> h28 xterm&
#./serverTCP.sh result_h19_h28_tcp_5m.txt
```

```
mininet> h27 xterm&
#./serverTCP.sh result_h20_h27_tcp_5m.txt
```

```
mininet> h26 xterm&
#./serverTCP.sh result_h21_h26_tcp_5m.txt
```

```
mininet> h25 xterm&
#./serverTCP.sh result_h22_h25_tcp_5m.txt
```

```
mininet> h24 xterm&
#./serverTCP.sh result_h23_h24_tcp_5m.txt
```


* Start your TCP clients in each host:

```
mininet> h18 xterm&
#./clientTCP.sh 10.0.0.12
```

```
mininet> h19 xterm&
#./clientTCP.sh 10.0.0.11
```

```
mininet> h20 xterm&
#./clientTCP.sh 10.0.0.10
```

```
mininet> h21 xterm&
#./clientTCP.sh 10.0.0.9
```

```
mininet> h22 xterm&
#./clientTCP.sh 10.0.0.8
```

```
mininet> h23 xterm&
#./clientTCP.sh 10.0.0.7
```

After measurements, you will be able to evaluate the performance results seeing the contenta of the generated *.txt files. Use you favorite software to read the files (Example: Gnumeric, R, Gnuplot, and others).


Virtual Machine 2: DPCTL 1.0
============================

Spanning Tree Protocol (STP) of Ryu is currently working properly only with DPCTL 1.0, so we provide a similar VM with this configuration.


* To run STP:

```
# ryu-manager ./simple_switch_stp_13.py
```

* In other terminal:

```
# mn --custom modeloMininet_1000serv_500vm.py --topo modeloMininet_1000serv_500vm --switch ovsk,protocols=OpenFlow13 --controller remote
```
