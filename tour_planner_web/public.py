from flask import *
from database import *

public=Blueprint('public',__name__)

@public.route('/')
def home():
    
    return render_template('home.html')

@public.route('/login',methods=['get','post'])
def login():
    if 'submit' in request.form:
        username=request.form['username']
        password=request.form['password']
        q="SELECT * FROM `login` WHERE `username`='%s' AND `password`='%s'"%(username,password)
        res=select(q)
        if res:
            user=res[0]['user_type']
            session['log_id']=res[0]['login_id']
            if user=='admin':

                return redirect(url_for('admin.admin_home'))
            if user=='provider':
                q="SELECT `provider_id` FROM `tour_provider` WHERE `login_id`='%s'"%(session['log_id'])
                res=select(q)
                if res:
                    session['pro_id']=res[0]['provider_id']
                return redirect(url_for('provider.provider_home'))
                flash('login success')
            if user=='guid':
                q="SELECT `guid_id` FROM `guid` WHERE `login_id`='%s'"%(session['log_id'])
                res=select(q)
                if res:
                    session['guid_id']=res[0]['guid_id']
                return redirect(url_for('guid.guid_home'))
                flash('login success')
        else:
            flash('ivalid username or password')
    return render_template("login.html")

@public.route('/provider_registration',methods=['get','post'])
def provider_registration():
    data={}
    if 'submit' in request.form:
        proname=request.form['proname']
        place=request.form['place']
        district=request.form['district']
        pincode=request.form['pincode']
        phone=request.form['phone']
        email=request.form['email']
        username=request.form['username']
        password=request.form['password']

        q="INSERT INTO `login` VALUES(NULL,'%s','%s','pending')"%(username,password)
        res=insert(q)
        q="INSERT INTO `tour_provider` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s')"%(proname,place,district,pincode,phone,email,res)
        insert(q)
    
    return render_template('provider_registration.html',data=data)



@public.route('/guid_registration',methods=['get','post'])
def guid_registration():
    data={}
    if 'submit' in request.form:
        fname=request.form['fname']
        lname=request.form['lname']
        place=request.form['place']
        phone=request.form['phone']
        email=request.form['email']
        lati=request.form['lati']
        longi=request.form['longi']
        place_id=request.form['place_id']
        username=request.form['username']
        password=request.form['password']

        q="INSERT INTO `login` VALUES(NULL,'%s','%s','pending')"%(username,password)
        res=insert(q)
        q="INSERT INTO `guid` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(res,place_id,fname,lname,place,phone,email,lati,longi)
        insert(q)
    qr="select * from places"
    data['view']=select(qr)
    return render_template('guid_registration.html',data=data)