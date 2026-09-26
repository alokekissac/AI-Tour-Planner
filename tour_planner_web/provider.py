from flask import *
from database import *
import uuid

provider=Blueprint('provider',__name__)

@provider.route('/provider_home')
def provider_home():
	
	return render_template('provider_home.html')

@provider.route('/provider_manage_place',methods=['get','post'])
def provider_manage_place():
	data={}
	q="select * from place_category"
	res=select(q)
	data['place_category']=res


	if 'place' in request.form:
		pcat=request.form['pcat']
		pname=request.form['pname']
		discription=request.form['discription']
		proimg=request.files['proimg']
		path='static/'+str(uuid.uuid4())+proimg.filename
		proimg.save(path)
		lati=request.form['lat']
		longi=request.form['lon']

		q="INSERT INTO `places` VALUES(NULL,'%s','%s','%s','%s','%s','%s')"%(pcat,pname,discription,path,lati,longi)
		res=insert(q)
	if 'action' in request.args:
		action=request.args['action']
		place_id=request.args['place_id']
	else:
		action=None

	if action=='remove':
		q="DELETE FROM `places` WHERE `place_id`='%s'"%(place_id)
		delete(q)
	q="select * from places"
	res=select(q)
	data['view']=res
	return render_template('provider_manage_place.html',data=data)


@provider.route('/provider_manage_tour_package',methods=['get','post'])
def provider_manage_tour_package():
	data={}
	q="select * from places"
	res=select(q)
	data['place_name']=res
	if 'submit' in request.form:
		pname=request.form['pname']
		place_id=request.form['place_id']
		amount=request.form['amount']
		days=request.form['days']
		nights=request.form['nights']
		adult=request.form['adult']
		child=request.form['child']
		q="INSERT INTO `packages` VALUES (NULL,'%s','%s','%s','%s','%s','%s','%s','%s','pending')"%(session['pro_id'],pname,place_id,amount,days,nights,adult,child)
		print(q)
		insert(q)
	if 'action' in request.args:
		action=request.args['action']
		pac_id=request.args['pac_id']
	else:
		action=None

	if action=='remove':
		q="DELETE FROM `packages` WHERE `package_id`='%s'"%(pac_id)
		delete(q)
	q="select * from packages"
	data['view']=select(q)
	return render_template('provider_manage_tour_package.html',data=data)

@provider.route('/provider_view_booking',methods=['get','post'])
def provider_view_booking():
	data={}
	# q="SELECT * FROM `packages` INNER JOIN `booking` USING(`package_id`) INNER JOIN `customer` USING(`customer_id`) WHERE login_id='%s'"%(session['pro_id'])
	q="SELECT * FROM `packages` INNER JOIN `booking` USING(`package_id`) INNER JOIN `customer` USING(`customer_id`)"
	print(q)
	res=select(q)
	data['view']=res
	
	if 'action' in request.args:
		action=request.args['action']
		b_id=request.args['b_id']
	else:
		action=None

	if action=='accept':
		q="UPDATE `booking` SET `booking_status`='confirmed' WHERE `booking_id`='%s'"%(b_id)
		update(q)
		flash ("booking confirmed")
		return redirect(url_for('provider.provider_view_booking'))
	return render_template('provider_view_booking.html',data=data)

@provider.route('/provider_view_enquiries',methods=['get','post'])
def provider_view_enquiries():
	data={}
	# q="SELECT * FROM `enquiry` INNER JOIN `customer` USING(`customer_id`) where login_id='%s'"%(session['pro_id'])
	q="SELECT * FROM `enquiry` INNER JOIN `customer` USING(`customer_id`)"
	print(q)
	res=select(q)
	data['view']=res
	j=0
	for i in range(1,len(res)+1):
		if 'submit' +str(i) in request.form:
			reply_details=request.form['reply_details'+str(i)]
			q="UPDATE `enquiry` SET `reply_details`='%s' WHERE `enquiry_id`='%s' "%(reply_details,res[j]['enquiry_id'])
			update(q)
			flash('success')
			return redirect(url_for('provider.provider_view_enquiries'))
		j=j+1
	
	return render_template('provider_view_enquiries.html',data=data)

