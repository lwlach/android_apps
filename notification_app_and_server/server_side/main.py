import socket
import _thread

c = 0

address = "192.168.1.202"
port = 101

server = socket.socket()
server.bind((address, port))
server.listen(100)

msg = "msg padrão"
msg_aux = "diferente"


def new_msg():
    global msg
    while True:
        msg = input()
        print("mensagem = {}".format(msg))
        if msg == "close now":
            server.close()


def new_client(connection, n):
    global msg, msg_aux
    print("Connection from: {}".format(add))
    while True:
        if msg_aux != msg:
            print("new_client {}".format(n))
            try:
                connection.send(msg.encode("UTF-8"))
                msg_aux = msg
            except:
                break
    connection.close()


while True:
    print("mensagem = {}".format(msg))
    conn, add = server.accept()
    c += 1
    _thread.start_new_thread(new_client, (conn, c, ))
    if msg == "msg padrão":
        _thread.start_new_thread(new_msg, ())


