#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json

class RemoteNode:
    def __init__(self, address, status, value):
        self.address = address
        self.type = 'Actuator'
        self.status = status
        self.value = value


def init_nodes():
    nodes_list.append(RemoteNode(5,'Online',0))
    nodes_list.append(RemoteNode(6,'Online',1))
    nodes_list.append(RemoteNode(7,'Offline',0))
    nodes_list.append(RemoteNode(8,'Online',0))


def list_remote_nodes():
    for node in nodes_list:
        print("Node {}, status {}, value {}".format(node.address, node.status, node.value))

def add_remote_node():
    newNode = RemoteNode(len(nodes_list)+5, 'Online', 0)
    nodes_list.append(newNode)
    print("Nodo agregado con direccion {}".format(newNode.address))
    node = {'address': str(newNode.address), 'status': newNode.status, 'value': newNode.value, 'type': newNode.type}
    return json.dumps(node)

def set_remote_node_status(address: int, status: str):
    address = address-5
    nodes_list[address].status = status
    print("Nodo con direccion {} seteado {}".format(nodes_list[address].address, nodes_list[address].status))
        
nodes_list = []
init_nodes()
