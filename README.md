TrafficEvaluator for NoCs
====

traffic-eval is a tool capable of evaluate and analyze the result data generated during a simulation of the NoC. 
It´s mainly based on the [Atlas Perfomance Evaluation tool](https://corfu.pucrs.br/redmine/projects/atlas/wiki/Performance_Evaluation).

The network currently supported is [Thor](https://github.com/LaNoC-UFC/NoCThor).

## Input parameters
traffic-eval can receive one or two parameters from command line.
* The first parameter is the full path to the files generated during the Noc simulation. This parameter is optional, 
if no path is passed, the files will be read from the __"evaluate"__ folder in the source directory. 
* The second parameter is the path for save the files generated after the evaluation. This parameter is optional, 
if no path is passed, the files will be saved in the __"results"__ folder in the source directory.

## Input files
traffic-eval needs a specific tree of directory and a specific format of file in order to do the evaluation.
For each traffic injection must be a folder with all files generated from NoC simulation and for each pair of routers
a file must be written in the following way:
* Each line consists of some attributes witch corresponds to a package.
* Each package have to obey the following order (all attributes are separated by a single space):
  * targetAddress(in hexadecimal format)
  * sizeOfPacket (in hexadecimal format)
  * sourceAddress (in hexadecimal format)
  * ID (1st part, in hexadecimal format)
  * ID (2nd part, in hexadecimal format)
  * Time of the arrival of the first flit (in decimal format).
  * Latency (in decimal format).
 
Example of the tree of directories: 
    
    -- bitReversal/
       --F001 // traffic injection of 1%
       --F002 // traffic injection of 2%     
       --FN   // traffic injection of N%

Example of lines that represents a package:

    -- Packages
       -- 0001 000D 0200 0000 03E9 150001 40 // 1st package
       -- 0001 000D 0200 0000 03EA 151501 40 // 2nd package    
       -- 0001 000D 0200 0000 03EB 153001 40 // 3th package
       -- ...                                // Nth package

## Output files

traffic-eval generates the following files for each input file:
* A Chaos Normal Form data of latency and the accepted traffic in function of the offered load;
* A Spatial Distribution (Histogram) of latency for each pair of routers;  
* A Spatial Distribution (Histogram) of accepted traffic for each pair of routers;
* A report with information about latency (min, med, max, standard deviation) and total accepted traffic.

All the above files (excepted the report) will consist of text data, where each line represent the 'x' and 'y' axis
separated by a single space.

Example:

    -- File of CNF / SD of Lat / SD of AT
       -- 0.010 0.010 // 1st coordinate
       -- 0.020 0.020 // 2nd coordinate
       -- nX    nY    // Nth coordinate
       

## Related resources
* [Thor](https://github.com/LaNoC-UFC/NoCThor)
* [traffic-eval](https://github.com/LaNoC-UFC/traffic-eval)
* [Atlas](https://corfu.pucrs.br/redmine/projects/atlas)