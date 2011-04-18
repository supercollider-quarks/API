import string,cgi,time
import sys
from os import curdir, sep
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer

# requires pyOSC
# https://trac.v2.nl/wiki/pyOSC
from OSC import OSCClient,OSCMessage, OSCServer


#  not working yet



class JSONHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        try:
            import ipdb; ipdb.set_trace()
            
            msg = OSCMessage(self.path)
            # look for ?args=json
            # but can only handle a flat array
            # needs to be encoded or via POST
            # and order is important
            # and type is important
            # msg.append('something')
            
            # OSCClientError: OSCClientError()

            client.send(msg)
            
            self.send_response(200)
            self.send_header('Content-type',    'text/html')
            self.end_headers()
            self.wfile.write("OK")
                
            return
                
        except Exception,e:
            raise e
            self.send_error(500,'I am broken: %s' % self.path)
     

    # def do_POST(self):
    #     global rootnode
    #     try:
    #         ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
    #         if ctype == 'multipart/form-data':
    #             query=cgi.parse_multipart(self.rfile, pdict)
    #         self.send_response(301)
            
    #         self.end_headers()
    #         upfilecontent = query.get('upfile')
    #         print "filecontent", upfilecontent[0]
    #         self.wfile.write("<HTML>POST OK.<BR><BR>");
    #         self.wfile.write(upfilecontent[0]);
            
    #     except :
    #         pass




if __name__ == '__main__':
    # will get better args
    if sys.argv[1:]:
        http_port = int(sys.argv[1])
    else:
        http_port = 8000
    osc_host = "127.0.0.1"
    osc_port = 12000
    try:
        sc = OSCServer( (osc_host,osc_port) )
        client = OSCClient(sc)
        server = HTTPServer(('', http_port), JSONHandler)
        print 'started http to OSC bridge...'
        server.serve_forever()
    except KeyboardInterrupt:
        print '^C received, shutting down server'
        server.socket.close()
        sc.close()
