from flask import Flask, request, jsonify, render_template
from RSSIknn import localiza, re_treino
from filtroDados import new_location

app = Flask(__name__)


@app.route('/teste')
def index():
    return '<h1>Server RSSI<h1>'

@app.route('/teste_postman', methods=['GET'])
def get_local_teste():
    ap = request.args.getlist("ap")
    return '<h1>'+localiza(ap)+'<h1>'


@app.route('/re')
def index2():
    re_treino();
    return '<h1>Server RSSI<h1>'


@app.route('/add_local', methods=['POST'])
def put_local():
    json = request.get_json()
    name = request.args.get("name")
    print(json['RSSI8'])
    print(name)
    new_location(name,json['RSSI8'])
    return "funcionou"
"""
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
"""

@app.route('/locate', methods=['GET'])
def get_local():
    ap = request.args.getlist("ap")
    print(localiza(ap))
    return localiza(ap)

@app.route('/result')
def result():
   dict = {'phy':50,'che':60,'maths':70}
   return render_template('result.html', result = dict)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
