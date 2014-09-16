#!/bin/bash

dot -Tps $1 -o $1.ps
ps2eps -f $1.ps 
epspdf -b $1.eps