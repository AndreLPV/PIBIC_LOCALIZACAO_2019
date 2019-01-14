## Flask

Utilizou-se o qeb framework Flask para implementar um servidor. Este servidor foi posteriormente carregado em um servidor em nuvem, especificamente na platafoma heroku.

# Configuração

## Localmente

pip install virtualenv
pip install virtualenvwrapper-win

mkdir RSSIFlask
type nul > __init__.py
type nul > app.py
cd RSSIFlask
virtualenv RSSIFlask_env

RSSIFlask_env\Scripts\activate

pip install flask
pip install gunicorn
pip install ipython
pip install numpy
pip install pandas
pip install -U scikit-learn

pip freeze > requirements.txt

python app.py

## Upload heroku

*Precisa ter configurado localmente e ter a ferramenta git instalada, além de ter seguido os passos de instalação do heroku

git init
git add .
git commit -m "first commit"
heroku create
git push heroku master
