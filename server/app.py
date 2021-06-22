from flask import Flask, request
import sqlite3 as sql
import json
from datetime import datetime
import random

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Nothing here'


@app.route('/v1/<vod_id>', methods=['POST', 'GET'])
def vod_endpoint(vod_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row

                cur = con.cursor()
                cur.execute("select * from vodi WHERE id_voda = %i" % int(vod_id))
                vodi = cur.fetchall()

                cur = con.cursor()
                cur.execute("select * from otroci WHERE id_voda = %i" % int(vod_id))
                otroci = cur.fetchall()
                otroci_obj = {}
                for otrok in otroci:
                    otroci_obj[otrok[0]] = {
                        "ime": otrok[1],
                        "priimek": otrok[2],
                        "naslov": otrok[3],
                        "številka": otrok[4]
                    }

                response = {
                    "ime": vodi[0][2],
                    "priimek": vodi[0][3],
                    "tab_ime": vodi[0][4],
                    "rod": vodi[0][1],
                    "vod": vodi[0][5],
                    "otroci": otroci_obj
                }
                return response
        except Exception as e:
            print(e)
            return "ne dela"

@app.route('/v1/<vod_id>/otroci', methods=['POST', 'GET'])
def otroci_endpoint(vod_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("select * from otroci WHERE id_voda = %i" % int(vod_id))
                otroci = cur.fetchall()
                otroci_obj = {"otroci":[]}
                for otrok in otroci:
                    otroci_obj["otroci"].append({
                        "id": otrok[0],
                        "ime": otrok[1],
                        "priimek": otrok[2],
                        "naslov": otrok[3],
                        "številka": otrok[4]
                    })

                return otroci_obj
        except Exception as e:
            print(e)
            return "ne dela"
    if request.method == 'POST':
        try:
            data = request.get_json(force=True)
            print("-----------POST------------")
            print(data)
            id_otroka = random.randint(0, 100000)
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("INSERT INTO otroci VALUES (%i,'%s','%s','%s','%s','',%i);" % (int(id_otroka),str(data["ime"]),str(data["priimek"]), str(data["naslov"]), str(data["stevilka"]), int(vod_id)))

                return "ok"
        except Exception as e:
            print(e)
            return "ne dela"

@app.route('/v1/<vod_id>/otroci/<otrok_id>', methods=['POST', 'GET'])
def otrok_endpoint(vod_id,otrok_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("select * from otroci WHERE id_otroka = %i AND id_voda = %i" % (int(otrok_id), int(vod_id)))
                otrok = cur.fetchall()[0]
                otrok_obj = {
                        "ime": otrok[1],
                        "priimek": otrok[2],
                        "naslov": otrok[3],
                        "številka": otrok[4]
                    }

                return otrok_obj
        except Exception as e:
            print(e)
            return "ne dela"
    if request.method == 'POST':
        try:
            data = request.get_json(force=True)
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("UPDATE otroci SET ime = %s, priimek = %s, naslov= %s, stevilka = %s WHERE id_otroka = %i" % (data["ime"], data["priimek"], data["naslov"], data["stevilka"], int(data["id"])))

                return "ok"
        except Exception as e:
            print(e)
            return "ne dela"


@app.route('/v1/<vod_id>/otroci/<otrok_id>/sestanki', methods=['POST', 'GET'])
def otrok_sestanek_endpoint(vod_id,otrok_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("SELECT * FROM srecanja sr INNER JOIN prisotnost p on sr.id_srecanja = p.id_srecanja INNER JOIN otroci o on o.id_otroka = p.id_otroka WHERE o.id_otroka = %i AND o.id_voda = %i ORDER BY sr.datum DESC;" % (int(otrok_id), int(vod_id)))

                srecanja = cur.fetchall()
                srecanje_obj = {"srecanja": []}
                for srecanje in srecanja:

                    srecanje_obj["srecanja"].append({
                        "id": srecanje[0],
                        "datum": datetime.strftime(datetime.strptime(srecanje[1], "%Y-%m-%d"), "%d.%m.%Y"),
                        "tema": srecanje[2],
                        "cilji": srecanje[3],
                        "prostor": srecanje[4],
                        "veščine": srecanje[5],
                        "opis": srecanje[6]
                    })

                return srecanje_obj
        except Exception as e:
            print(e)
            return "ne dela"

@app.route('/v1/<vod_id>/srecanja/<srecanje_id>', methods=['POST', 'GET'])
def srecanje_endpoint(vod_id,srecanje_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("select * from srecanja WHERE id_srecanja = %i AND id_voda = %i" % (int(srecanje_id), int(vod_id)))
                srecanje = cur.fetchall()[0]

                cur = con.cursor()
                cur.execute("SELECT * FROM otroci o INNER JOIN prisotnost pr ON pr.id_otroka = o.id_otroka INNER JOIN srecanja s ON s.id_srecanja = pr.id_srecanja WHERE s.id_srecanja = %i" % int(srecanje_id))
                otroci = cur.fetchall()
                otroci_obj = ""
                for otrok in otroci:
                    otroci_obj += "-" + otrok[1] + " " + otrok[2] + "\n"

                srecanje_obj = {
                        "datum": srecanje[1],
                        "tema": srecanje[2],
                        "cilji": srecanje[3],
                        "prostor": srecanje[4],
                        "veščine": srecanje[5],
                        "opis": srecanje[6],
                        "otroci": otroci_obj
                    }

                return srecanje_obj
        except Exception as e:
            print(e)
            return "ne dela"

@app.route('/v1/<vod_id>/srecanja/', methods=['POST', 'GET'])
def srecanja_endpoint(vod_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("select * from srecanja WHERE id_voda = %i ORDER BY datum DESC" % int(vod_id))
                srecanja = cur.fetchall()
                srecanje_obj = {"srecanja":[]}
                for srecanje in srecanja:
                    cur = con.cursor()
                    cur.execute("SELECT * FROM otroci o INNER JOIN prisotnost pr ON pr.id_otroka = o.id_otroka INNER JOIN srecanja s ON s.id_srecanja = pr.id_srecanja WHERE s.id_srecanja = %i" % int(srecanje[0]))
                    otroci = cur.fetchall()
                    otroci_obj = ""
                    for otrok in otroci:
                        otroci_obj += "-" + otrok[1] + " " + otrok[2] + "\n"

                    srecanje_obj["srecanja"].append({
                            "id":srecanje[0],
                            "datum": datetime.strftime(datetime.strptime(srecanje[1],"%Y-%m-%d"),"%d.%m.%Y"),
                            "tema": srecanje[2],
                            "cilji": srecanje[3],
                            "prostor": srecanje[4],
                            "veščine": srecanje[5],
                            "opis": srecanje[6],
                            "otroci": otroci_obj[:-2]
                        })

                return srecanje_obj
        except Exception as e:
            print(e)
            return "ne dela"
    if request.method == 'POST':
        try:
            data = request.get_json(force=True)
            print("-----------POST - srecanja ------------")
            print(data)
            id_srecanja = random.randint(0, 100000)
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("INSERT INTO srecanja VALUES (%i,'%s','%s','%s','%s','%s','%s',%i);" % (int(id_srecanja), str(datetime.strftime(datetime.strptime(data["datum"],"%d.%m.%Y"),"%Y-%m-%d")), str(data["tema"]), str(data["cilji"]), str(data["prostor"]),str(data["vescine"]),str(data["opis"]),int(vod_id)))
            for otrok in data["prisotni"]:
                with sql.connect("baza.db") as con:
                    con.row_factory = sql.Row
                    cur = con.cursor()
                    cur.execute("INSERT INTO prisotnost VALUES (%i,%i);" % (int(otrok), int(id_srecanja)))
            return "ok"
        except Exception as e:
            print(e)
            return "ne dela"

@app.route('/v1/<vod_id>/prihodnja_srecanja/', methods=['POST', 'GET'])
def prihodnja_endpoint(vod_id):
    if request.method == 'GET':
        try:
            with sql.connect("baza.db") as con:
                con.row_factory = sql.Row
                cur = con.cursor()
                cur.execute("select * from prihodnja_srecanja WHERE id_voda = %i ORDER BY datum ASC" % int(vod_id))
                srecanja = cur.fetchall()
                srecanje_obj = {"srecanja":[]}
                for srecanje in srecanja:
                    srecanje_obj["srecanja"].append({
                            "id":srecanje[0],
                            "datum": datetime.strftime(datetime.strptime(srecanje[1],"%Y-%m-%d"),"%d.%m."),
                            "ura": srecanje[2],
                            "kaj": srecanje[3]
                        })

                return srecanje_obj
        except Exception as e:
            print(e)
            return "ne dela"

if __name__ == '__main__':
    app.run()
