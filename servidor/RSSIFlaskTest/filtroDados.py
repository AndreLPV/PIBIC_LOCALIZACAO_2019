from kalman import KalmanFilter
import csv
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from numpy import median

#url_data = "valores.txt"
url_data = "dados/valores.txt"
url_new_data = "dados/valoresNew.txt"
url_filter = "dados/valoresFilter.txt"
names = ['AP1', 'AP2', 'AP3', 'AP4', 'AP5', 'AP6', 'AP7', 'AP8', 'Class']

test = KalmanFilter(0.008, 5)
# Exemplo ruim
# plot_re_filter_ap('ap2','Centro-Sala')

# Exemplo bom
# plot_re_filter_ap('ap1','Canto-Sentido-Porta')

# Exemplo teste
# plot_re_filter_ap('ap3','Centro-Sala')

def filter_ap(data): 
    test = KalmanFilter(0.008,5)
    return [test.filter(x) for x in data]


def new_location(name_location, data_location):
    X = data_location
    y = name_location   
    ap1 = [x['ap1'] for x in X]
    ap2 = [x['ap2'] for x in X]
    ap3 = [x['ap3'] for x in X]
    ap4 = [x['ap4'] for x in X]
    ap5 = [x['ap5'] for x in X]
    ap6 = [x['ap6'] for x in X]
    ap7 = [x['ap7'] for x in X]
    ap8 = [x['ap8'] for x in X]
    for a1,a2,a3,a4,a5,a6,a7,a8 in zip(ap1,ap2,ap3,ap4,ap5,ap6,ap7,ap8):
        write_data(a1,a2,a3,a4,a5,a6,a7,a8,y)


def new_location_home(name_location, data_location):
    X = data_location
    y = name_location   
    ap1 = [x['ap1'] for x in X]
    ap2 = [x['ap2'] for x in X]
    ap3 = [x['ap3'] for x in X]
    ap4 = [x['ap4'] for x in X]
    ap5 = [x['ap5'] for x in X]
    ap6 = [x['ap6'] for x in X]
    ap7 = [x['ap7'] for x in X]
    ap8 = [x['ap8'] for x in X]
    for a1,a2,a3,a4,a5,a6,a7,a8 in zip(ap1,ap2,ap3,ap4,ap5,ap6,ap7,ap8):
        write_data(a1,a2,a3,a4,a5,a6,a7,a8,y)


def new_location_filter(name_location, data_location):
    X = data_location
    y = name_location   
    ap1 = filter_ap([x['ap1'] for x in X])
    ap2 = filter_ap([x['ap2'] for x in X])
    ap3 = filter_ap([x['ap3'] for x in X])
    ap4 = filter_ap([x['ap4'] for x in X])
    ap5 = filter_ap([x['ap5'] for x in X])
    ap6 = filter_ap([x['ap6'] for x in X])
    ap7 = filter_ap([x['ap7'] for x in X])
    ap8 = filter_ap([x['ap8'] for x in X])
    for a1,a2,a3,a4,a5,a6,a7,a8 in zip(ap1,ap2,ap3,ap4,ap5,ap6,ap7,ap8):
        write_data_filter(a1,a2,a3,a4,a5,a6,a7,a8,y)


def write_data(a1,a2,a3,a4,a5,a6,a7,a8,local):
    file = open(url_new_data,"a+")
    file.write("{},{},{},{},{},{},{},{},{}\n".format(round(a1),round(a2),round(a3),round(a4),round(a5),round(a6),round(a7),round(a8),local))
    file.close()


def write_data_filter(a1,a2,a3,a4,a5,a6,a7,a8,local):
    file = open(url_filter,"a+")
    file.write("{},{},{},{},{},{},{},{},{}\n".format(round(a1),round(a2),round(a3),round(a4),round(a5),round(a6),round(a7),round(a8),local))
    file.close()


def re_filter():
    dataset = pd.read_csv(url_data, names=names)
    X = dataset.iloc[:, :-1].values
    y = dataset.iloc[:, 8].values
    open(url_filter,"w").close()
    locais = [x for x in y]
    locais = list(set(locais))
    # tam = len(locais)
    all = dataset.values
    for x in locais:
        a = [y for y in all if y[8] == x]
        ap1 = filter_ap([x[0] for x in a])
        ap2 = filter_ap([x[1] for x in a])
        ap3 = filter_ap([x[2] for x in a])
        ap4 = filter_ap([x[3] for x in a])
        ap5 = filter_ap([x[4] for x in a])
        ap6 = filter_ap([x[5] for x in a])
        ap7 = filter_ap([x[6] for x in a])
        ap8 = filter_ap([x[7] for x in a])
        for a1,a2,a3,a4,a5,a6,a7,a8 in zip(ap1,ap2,ap3,ap4,ap5,ap6,ap7,ap8):
            write_data_filter(a1,a2,a3,a4,a5,a6,a7,a8,x)


re_filter()


def plot_re_filter_ap(ap_name,local):
    dataset = pd.read_csv(url_data, names=names)
    y = dataset.iloc[:, 8].values
    locais = [x for x in y]
    locais = list(set(locais))
    # tam = len(locais)
    all = dataset.values
    if local in locais:
        a = [y for y in all if y[8] == local]
        result = {
            'ap1':[x[0] for x in a],
            'ap2':[x[1] for x in a],
            'ap3':[x[2] for x in a],
            'ap4':[x[3] for x in a],
            'ap5':[x[4] for x in a],
            'ap6':[x[5] for x in a],
            'ap7':[x[6] for x in a],
            'ap8':[x[7] for x in a],
        }.get(ap_name,[])
        if result != []:return plot_signal_ap(result)
        else: print('Especifique o ap: ap1,ap2,ap3...ap8')
    else:
        print ('local não encontrado nos dados, locais disponíveis:')
        for x in locais: print (x)


def plot_signal_ap(data):
    filtered = filter_ap(data)
    print("Variância: " + str(np.var(filtered)))
#    print(median(filtered))
    maxx = str(median(filtered)+np.var(filtered))
    minn = str(median(filtered)-np.var(filtered))
    print("max: "+ maxx)
    print("min: "+ minn)
    plt.figure()
    plt.plot(data,'k.--',label='Sinal com ruído')
    plt.plot(filtered,'b-',label='Sinal filtrado por Kalman')
    plt.plot([median(filtered)]*len(filtered),'g-',label='Média do sinal filtrado')
    plt.legend()
    plt.xlabel('Iteração')
    plt.ylabel('Sinal RSSI')
    plt.show()


plot_re_filter_ap('ap1','cima')