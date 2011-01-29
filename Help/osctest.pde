/**
 * basic send to SuperCollider
 * felix
 * requires osc library:
 * oscP5broadcastClient by andreas schlegel
 * oscP5 website at http://www.sojamo.de/oscP5
 */

import oscP5.*;
import netP5.*;


OscP5 oscP5;

/* a NetAddress contains the ip address and port number of a remote location in the network. */
NetAddress sc; 

void setup() {
  size(400,400);
  frameRate(25);
  
  /* create a new instance of oscP5. 
   * 12000 is the port number processing is listening for incoming osc messages.
   */
  oscP5 = new OscP5(this,50540);
  
  /* create a new NetAddress. a NetAddress is used when sending osc messages
   * with the oscP5.send method.
   */
  
  /* the address of the osc broadcast server */
  sc = new NetAddress("127.0.0.1",57120);
}


void draw() {
  background(0);
}


void mousePressed() {
  /* create a new OscMessage with an address pattern, in this case /myApp/wiggle . */
  OscMessage msg = new OscMessage("/myApp/wiggle");
  /* add a value (an integer) to the OscMessage */
  msg.add(4);
  println("sending");
  oscP5.send(msg, sc);
}



/* incoming osc message are forwarded to the oscEvent method. */
void oscEvent(OscMessage msg) {
  /* get and print the address pattern and the typetag of the received OscMessage */
  println("### received an osc message with addrpattern "+msg.addrPattern()+" and typetag "+msg.typetag());
  msg.print();
}
