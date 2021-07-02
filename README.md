# Software Engineering Project 2020-2021


Prof: Pierluigi San Pietro

#### Group: SP10

#### Students:
* Giaccaglia Pablo
* Laconca Alessandro
* Lodari Gianmarco 907448



## Implemented Features


| Syntax | Description |
| ----------- | ----------- |
| BASIC RULES | YES |
| COMPLETE RULES | YES |
| CLI | YES |
| GUI | YES |
| SOCKET | YES |
| AF1 | MULTIPLE MATCHES |
| AF2  | PERSISTENCE  |
| AF3 | DISCONNECTION RESILIENCE|




## Usage

### Windows

1. Open the command line
2. Clone the repository
3. In the repository's directory, run:
```bash
java -jar Maestri.jar
```
4. With the following arguments:
```bash
java -jar Maestri.jar [mode] [port] [ip] [nickname]

Modes:
  -c,--cli        Starts the CLI and connects to server with port, ip and nickname
                  without arguments it will let you choose them later
  -g,--gui        Starts the GUI and connects to server with port, ip and nickname
                  without arguments it will let you choose them later
  -s,--server     Starts server with portoose them later");
```

5. If you want to run the server, use the -s or the --server argument, along with the desired TCP port. You will be asked to insert a TCP port if it's not in the argument

6. If you want to run the client, use the -c or the --cli argument, along with the desired TCP port, IP addres and nickname. The user will be asked to insert them again if some of them are missing in the argument

7. If you want to run the GUI, use the -g or the --gui argument, along with the desired TCP port, IP addres and nickname. The user will be asked to insert them again if some of them are missing in the argument

##### GUI

* We implemented a 3D gui based on a simplified model of the server's state machine. The user can freely see their board, using W and S to cycle between Board view, Market view, and Frontal view, which covers every game component. While looking from the Frontal view, the player can use D and S keys to rotate around the table, with 90 degree steps. Both the CLI and GUI implement real time updates on the game state. Common elements, such as CardShop and ResourceMarket are shared between all players, each players sees them in front of their board but they are actually synchronized

## Testing
* All unit test have been automated when possible. For some network functionalities it has been necessary to perform some manual QA instead
  of a mock test , for which it would have required too many stubs (being a stateful protocol) so we choose
  manual Quality Assurance.

* All the model classes have been extensively covered, along with most of the controller's classes when possible. Some functionalities regarding
  production output conversion and leader activation do not have an automated test, but our manual tests led us to assert that the functionalities
  implemented are stable in all cases.

* Clicking repeatedly during the drag and drop action may cause issues.


* All the unit test have been run before each commit
