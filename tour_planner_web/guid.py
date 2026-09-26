from flask import *
from database import *

guid=Blueprint('guid',__name__)

@guid.route('/admin_home')
def guid_home():
    
    return render_template('guid_home.html')

@guid.route('/guid_mark_locations',methods=['get','post'])
def guid_mark_locations():
    data={}
    q="select * from places"
    res=select(q)
    data['place_name']=res
    if 'submit' in request.form:
        place=request.form['place_id']
        discription=request.form['discription']
        lati=request.form['lati']
        longi=request.form['longi']
        qr="INSERT INTO `markedlocation` VALUES(NULL,'%s','%s','%s','%s','%s')"%(place,lati,longi,discription,session['guid_id'])
        insert(qr)
    if 'action' in request.args:
        action=request.args['action']
        m_id=request.args['m_id']
    else:
        action=None
    if action == "delete":

        q="DELETE from markedlocation where mlocation_id='%s'"%(m_id)
        print(q)
        delete(q)
        flash("deleted successfully")
        
    qv="SELECT * FROM `markedlocation` inner join places using (place_id) WHERE `guid_id`='%s'"%(session['guid_id'])
    data['view']=select(qv)
    return render_template('guid_mark_locations.html',data=data)