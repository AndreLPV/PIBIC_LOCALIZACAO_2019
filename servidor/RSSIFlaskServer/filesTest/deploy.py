from flask import Flask, request, jsonify
from RSSIknn import localiza, re_treino

app = Flask(__name__)


@app.route('/teste')
def index():
    return '<h1>Server RSSI<h1>'

@app.route('/re')
def index2():
    re_treino();
    return '<h1>Server RSSI<h1>'


@app.route('/add_local', methods=['POST'])
def put_local():
    json = request.get_json()
    print(json)
    ap1 = json['ap1']
    ap2 = json['ap2']
    ap3 = json['ap3']
    ap4 = json['ap4']
    ap5 = json['ap5']
    ap6 = json['ap6']
    ap7 = json['ap7']
    ap8 = json['ap8']
    sinais = [ap1, ap2, ap3, ap4, ap5, ap6, ap7, ap8]
    print(sinais)
#   return jsonify({'local': localiza(sinais)})
    return localiza(sinais)


@app.route('/locate', methods=['GET'])
def get_local():
    ap = request.args.getlist("ap")
    return localiza(ap)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
