from flask import *
from database import *
import uuid
from translatetoanylang import *


from opai import *



try:
    import cv2  # optional: OCR feature
except ImportError:
    cv2 = None
import os
import requests
import io
import json
import uuid
try:
    import pytesseract  # optional: OCR feature
except ImportError:
    pytesseract = None
from PIL import Image
try:
    from test import checkans  # optional: NLTK similarity
except ImportError:
    checkans = None

api=Blueprint('api',__name__)



def ocrgenerate(path):
    if cv2 is None or pytesseract is None:
        return "OCR is not available in this deployment (install opencv-python + pytesseract)."
    print("path===", path)
    image = cv2.imread(path)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    filename = "{}.png".format("temp")
    cv2.imwrite(filename, gray)

    # On macOS, no need to set tesseract_cmd if installed with Homebrew
    text = pytesseract.image_to_string(Image.open(filename))

    print("OCR Text is " + text.strip())
    return text.strip()

# def ocrgenerate(path):
#     print("path===",path)
#     image = cv2.imread(path)
#     # print(image)
#     gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

#     # Store grayscale image as a temporary file to apply OCR
#     filename = "{}.png".format("temp")
#     cv2.imwrite(filename, gray)

#     # Load the image as a PIL/Pillow image, apply OCR, and then delete the temporary file
#     pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
#     text = pytesseract.image_to_string(Image.open(filename))

#     print("OCR Text is " + text.strip())
#     # val=text.strip().split('D')
#     # atte=val[0].split(',')
#     # print(atte[0],atte[1])
#     # # print(val[1])
#     return  text.strip()








@api.route('/Student_upload_anwer',methods=['get','post'])
def Student_upload_anwer():
	out=""
	data={}
	image=request.files['image1']
	path=save_upload(image,str(uuid.uuid4())+".png")
	val=ocrgenerate(os.path.join(ROOT,'public',path) if path.startswith('static/') else os.path.join('/tmp',path))
	# print("------------------------------------------------------") 
	# print("helooooooi",val)
	# print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
	
	# q="SELECT * FROM `handwriting` WHERE `handwriting_id`='%s'"%(handwriting_ids)
	# print(q)
	# res=select(q)
	# oans = image
	# print(oans)
	# mark=0.50
	# sim = checkans(oans,val)
	# print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
	# print(sim)
	# print("ooooooooooooooooooooooooooooooooooooooooooooooooo")
	# if float(sim) < float(mark):
	# 	out="Dysgraphia disease Detect"
	# else:
	# 	out="No Disease Detect"
	# print("=====================================================")
	# print(out)
	# print("=====================================================")
	data['status']="success"
	data['method']="Student_upload_anwer"
	data['val']=val
	# print("+++++++++++++++++++",data['val'])
	return str(data)


@api.route('/filetrasilation',methods=['get','post'])
def filetrasilation():
	data = {}

	language=request.args['language']
	texttran=request.args['texttran']
	out= transs(texttran,language)
	print(out,"111111111111111111111")
	print(out)
	if out:
		data['method']="fileouttrans"
		data['status'] = 'success'
		data['out'] = out
	return str(data)






@api.route('/trasilation',methods=['get','post'])
def trasilation():
	data = {}

	language=request.args['language']
	texttran=request.args['texttran']
	out= transs(texttran,language)
	print(out,"111111111111111111111")
	if out:
		data['status'] = 'success'
		data['out'] = out
	return str(data)

@api.route('/login')
def login():
	data={}
	username=request.args['username']
	password=request.args['password']
	latti=request.args['latti']
	longi=request.args['longi']
	q="SELECT * FROM login WHERE `username`='%s' AND `password`='%s'"%(username,password)
	res=select(q)
	q="UPDATE `customer` SET `latitude`='%s' , `longitude`='%s' where login_id='%s'"%(latti,longi,res[0]['login_id'])
	update(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	return str(data)
@api.route('/updatepasslocation')
def updatepasslocation():
	data={}
	latti=request.args['latti']
	longi=request.args['longi']
	lid=request.args['lo_gid']
	if lid=='null':
		pass
	else:
     
		q="UPDATE `customer` SET `latitude`='%s' , `longitude`='%s' where login_id='%s'"%(latti,longi,lid)
		update(q)
	return str(data)

@api.route('/Customer_registration')
def Customer_registration():
	data={}
	fname=request.args['fname']
	lanme=request.args['lname']
	hname=request.args['hname']
	place=request.args['place']
	district=request.args['district']
	country=request.args['country']
	pin=request.args['Pin']
	phone=request.args['phone']
	email=request.args['email']
	passport=request.args['passport']
	gender=request.args['gender']
	dob=request.args['dob']
	username=request.args['uname']
	password=request.args['pass']
	# lati=request.args['lati']
	# longi=request.args['logi']

	q1="SELECT * FROM login WHERE `username`='%s'"%(username)
	res=select(q1)
	if res:
		data['status']="duplicate"
		data['method']="Customer_registration"
	else:

		q=" INSERT INTO `login` VALUES(NULL,'%s','%s','customer')"%(username,password)
		print(q)
		result=insert(q)
		qr="INSERT INTO `customer` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','0','0')"%(fname,lanme,hname,place,district,country,pin,phone,email,passport,gender,dob,result)
		id=insert(qr)
		if id > 0:
			data['status']="success"
		else:
			data['status']="failed"
		data['method']="Customer_registration"	
	return str(data)

@api.route('/Customer_view_providers')
def Customer_view_providers():
	data={}
	q="SELECT * FROM `tour_provider`"
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_providers"
	return str(data)

@api.route('/view_places')
def view_places():
	data={}
	# q="SELECT * FROM `packages`,`places`,`place_category` WHERE `packages`.`package_id` AND `place_category`.`category_id` AND `places`.`place_id` GROUP BY places.`place_name`"
	q="SELECT * FROM `packages` INNER JOIN `places` ON `packages`.`places`=`places`.`place_id` INNER JOIN `place_category` ON `place_category`.`category_id`=`places`.`category_id` GROUP BY `place_name`"
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="view_places"
	return str(data)
@api.route('/searchplace')
def searchplace():
	data={}
	# log_id=request.args['log_id']
	searchitem='%'+request.args['searchitem']+'%'
	# q="SELECT * FROM `places` WHERE `place_name` LIKE '%s'"%(searchitem)
	# q="SELECT * FROM `packages`,`places`,`place_category` WHERE `packages`.`package_id` AND `place_category`.`category_id` AND `places`.`place_id` AND `place_name`  like '%s'"%(searchitem)
	q="SELECT * FROM `packages` INNER JOIN `places` ON `packages`.`places`=`places`.`place_id` INNER JOIN `place_category` ON `place_category`.`category_id`=`places`.`category_id` where `place_name`  like '%s' "%(searchitem)
	print(q)
	res=select(q)
	data['status']="success"
	data['data']=res
	data['method']="view_places"
	return str(data)

@api.route('/customer_add_favorite')
def customer_add_favorite():
	data={}
	log_id=request.args['log_id']
	package_id=request.args['package_id']
	q=" SELECT * FROM `favorites` WHERE `package_id`='%s' AND `customer_id`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(package_id,log_id)
	id=select(q)
	if id:
		data['status']="failed"
	else:
		q="INSERT INTO `favorites` VALUES(null,'%s',(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s' ))"%(package_id,log_id)
		print(q)
		insert(q)
		data['status']="success"
	data['method']="Customer_add_to_favorite"
	return str(data)

@api.route('/Customer_view_favorite')
def Customer_view_favorite():
	data={}
	log_id=request.args['log_id']
	pac_id=request.args['pac_id']
	q="  SELECT * FROM `favorites` INNER JOIN `packages` USING(`package_id`) WHERE `customer_id`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s') AND `package_id`='%s'"%(log_id,pac_id)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_favorite"
	
	return str(data)

@api.route('/fev_delete')
def fev_delete():
	data={}
	favorite_ids=request.args['favorite_ids']
	q="DELETE FROM `favorites` WHERE `favorite_id`='%s'"%(favorite_ids)
	print(q)
	res=delete(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_favorite"
	return str(data)

@api.route('/Customer_booking_tour')
def Customer_booking_tour():
	data={}
	log_id=request.args['log_id']
	package_ids=request.args['package_ids']
	quantity=request.args['quantity']
	totamount=request.args['totamount']
	date=request.args['date']
	q="INSERT INTO `booking` VALUES(NULL,'%s',(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s'),'%s',CURDATE(),'%s','%s','pending')"%(package_ids,log_id,quantity,date,totamount)
	print(q)
	res=insert(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_booking_tour"
	return str(data)
@api.route('/view_booking')
def view_booking():
	data={}
	log_id=request.args['log_id']
	q="SELECT * FROM `booking` WHERE `customer_id`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(log_id)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="view_booking"
	
	return str(data)


@api.route('view_enquiries')
def view_enquiries():
	data={}
	log_id=request.args['log_id']
	pro_id=request.args['pro_id']
	q="SELECT * FROM `enquiry` WHERE `customer_id`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s') AND `provider_id`='%s'"%(log_id,pro_id)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['data']="failed"
	data['method']="view_enquiries"
	return str(data)



@api.route('/Review')
def Review():
    
	data={}
	log_id=request.args['log_id']
	package_ids=request.args['package_ids']
	rating=request.args['rating']
	review=request.args['review']
	q="SELECT * FROM `review_ratings` WHERE `package_id`='%s'  AND `customer`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(package_ids,log_id)
	res=select(q)
	if res:
		q="UPDATE `review_ratings` SET `rating`='%s' , `review`='%s' ,`datetime`=now() WHERE `package_id`='%s'"%(rating,review,package_ids)
		update(q)
		data['status'] = 'success'
	else:
		q="INSERT INTO `review_ratings`  VALUES(NULL,'%s',(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s'),'%s','%s',now())"%(package_ids,log_id,rating,review)
		id=insert(q)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'Review'
	return str(data)

@api.route("/viewrating")
def viewrating():
	data={}
	package_ids=request.args['package_ids']
	log_id=request.args['log_id']
	q="SELECT * FROM `review_ratings` WHERE `package_id`='%s' AND `customer`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(package_ids,log_id)
	print(q)
	val=select(q)
	if val:
		data['status'] = 'success'
		data['data'] = val[0]['rating']
		data['data1'] = val[0]['review']
	else:
		data['status'] = 'failed'
	data['method']="viewrating" 
	return str(data)

@api.route('/Customer_send_enquiries')
def Customer_send_enquiries():
	data={}
	log_id=request.args['log_id']
	pro_id=request.args['pro_id']
	enquiries=request.args['enquiries']
	q="INSERT INTO `enquiry` VALUES(NULL,(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s'),'%s','%s','pending',NOW())"%(log_id,pro_id,enquiries)
	print(q)
	res=insert(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_send_enquiries"
	return str(data)


@api.route('/view_complaint')
def view_complaint():
    data={}
    log_id=request.args['log_id']
    q="SELECT * FROM `complaint` WHERE `customer_id`=(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(log_id)
    res=select(q)
    if res:
        data['status']="success"
        data['data']=res
    else:
        data['status']="failed"
    data['method']="view_complaint"
    return str(data)

@api.route('/Customer_send_complaint')
def Customer_send_complaint():
    data={}
    log_id=request.args['log_id']
    complaint=request.args['complaint']
    q="INSERT INTO `complaint` VALUES(NULL,(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s'),'%s','pending',NOW())"%(log_id,complaint)
    print(q)
    res=insert(q)
    if res:
        data['status']="success"
        data['data']=res
    else:
        data['status']="failed"
    data['method']="Customer_send_complaint"
    return str(data)

@api.route('/UserViewChats')
def UserViewChats():
	data={}
	log_id=request.args['login_id']
	q="SELECT * FROM `customer` WHERE customer_id<>(SELECT `customer_id` FROM `customer` WHERE `login_id`='%s')"%(log_id)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="UserViewChats"
	return str(data)

@api.route('/Customer_view_marked_location')
def Customer_view_marked_location():
	data={}
	q="SELECT * FROM `markedlocation`"
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_marked_location"
	return str(data)

@api.route('/Customer_view_guid')
def Customer_view_guid():
	data={}
	q="SELECT * FROM `guid` INNER JOIN  `places` USING(`place_id`)"
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_guid"
	return str(data)

@api.route('/Customer_view_booking')
def Customer_view_booking():
	data={}
	log_id=request.args['log_id']
	# q="SELECT * FROM `booking` WHERE (SELECT `customer_id` FROM `customer` WHERE login_id='%s')"%(log_id)
	q="SELECT * FROM `booking` INNER JOIN `packages` USING(`package_id`) WHERE (SELECT `customer_id` FROM `customer` WHERE login_id='%s')"%(log_id)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Customer_view_booking"
	return str(data)



@api.route('/payment')
def payment():
	data={}
	# log_id=request.args['log_id']
	amounts=request.args['amounts']
	booking_id=request.args['booking_id']
	q="INSERT INTO `payment` VALUE(NULL,'%s','%s','cardpayment',NOW())"%(booking_id,amounts)
	print(q)
	res=insert(q)
	qr="UPDATE `booking` SET `booking_status`='paid' WHERE `booking_id`='%s'"%(booking_id)
	update(qr)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="package_id"
	return str(data)

@api.route('/chat',methods=['get','post'])
def chat():

	data = {}

	sender_id=request.args['sender_id']
	receiver_id=request.args['receiver_id']
	details=request.args['details']


	qr="INSERT INTO`chat` VALUES(NULL,'%s',(SELECT `login_id` FROM `customer` WHERE `customer_id`='%s'),'%s',CURDATE())"%(sender_id,receiver_id,details)
	print(qr)
	id=insert(qr)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method']='chat'
	return str(data)


@api.route('/chatdetail',methods=['get','post'])
def chatdetail():
	data = {}

	sender_id=request.args['sender_id']
	receiver_id=request.args['receiver_id']
	
	
	q="SELECT * FROM `chat` WHERE (`sender_id`='%s' AND `receiver_id`=(SELECT `login_id` FROM `customer` WHERE `customer_id`='%s')) OR (`sender_id`=(SELECT `login_id` FROM `customer` WHERE `customer_id`='%s') AND `receiver_id`='%s')"%(sender_id,receiver_id,receiver_id,sender_id)
	res = select(q)
	print(q)
	if res:
		data['status'] = 'success'
		data['data'] = res
		
	else:
		data['status'] = 'failed'
	data['method'] = 'chatdetail'
	return str(data)



@api.route('/image',methods=['get','post'])
def image():
	data = {}

	data['status'] = 'success'
	data['data'] = "static/8444c43f-b52f-4fb7-981a-f719c1883acea.jpg"
		
	data['method'] = 'chatdetail'
	return str(data)

@api.route('/Public_view_places')
def Public_view_places():
	data={}
	# q="SELECT * FROM `packages`,`places`,`place_category` WHERE `packages`.`package_id` AND `place_category`.`category_id` AND `places`.`place_id` GROUP BY places.`place_name`"
	q="SELECT * FROM `packages` INNER JOIN `places` ON `packages`.`places`=`places`.`place_id` INNER JOIN `place_category` ON `place_category`.`category_id`=`places`.`category_id` GROUP BY `place_name`"
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="Public_view_places"
	return str(data)
@api.route('/publicsearch')
def publicsearch():
	data={}
	# log_id=request.args['log_id']
	searchitem='%'+request.args['searchitem']+'%'
	# q="SELECT * FROM `places` WHERE `place_name` LIKE '%s'"%(searchitem)
	# q="SELECT * FROM `packages`,`places`,`place_category` WHERE `packages`.`package_id` AND `place_category`.`category_id` AND `places`.`place_id` AND `place_name`  like '%s'"%(searchitem)
	q="SELECT * FROM `packages` INNER JOIN `places` ON `packages`.`places`=`places`.`place_id` INNER JOIN `place_category` ON `place_category`.`category_id`=`places`.`category_id` where pack_status='Public' and `place_name`  like '%s' "%(searchitem)
	print(q)
	res=select(q)
	data['status']="success"
	data['data']=res
	data['method']="Public_view_places"
	return str(data)



@api.route('/User_upload_images',methods=['get','post'])
def User_upload_images():
	out=""
	data={}
	image=request.files['image1']
	path=save_upload(image,str(uuid.uuid4())+".png")
	val=ocrgenerate(os.path.join(ROOT,'public',path) if path.startswith('static/') else os.path.join('/tmp',path))
	# print("------------------------------------------------------") 
	# print("helooooooi",val)
	# print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
	
	# q="SELECT * FROM `handwriting` WHERE `handwriting_id`='%s'"%(handwriting_ids)
	# print(q)
	# res=select(q)
	# oans = image
	# print(oans)
	# mark=0.50
	# sim = checkans(oans,val)
	# print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
	# print(sim)
	# print("ooooooooooooooooooooooooooooooooooooooooooooooooo")
	# if float(sim) < float(mark):
	# 	out="Dysgraphia disease Detect"
	# else:
	# 	out="No Disease Detect"
	# print("=====================================================")
	# print(out)
	# print("=====================================================")
	data['status']="success"
	data['method']="User_upload_images"
	data['val']=val
	# print("+++++++++++++++++++",data['val'])
	return str(data)

@api.route('/user_chat_ai_bot', methods=['GET', 'POST'])
def user_chat_ai_bot():
    data = {}
    log_id = request.args['log_id']
    
    if 'submit' in request.form:
        user_input = request.form['textfield'].replace("'", "\\'")
        log_id = request.args['log_id']
        
        # Use the renamed function directly
        ss = chatbot_response(user_input)
        print(user_input)
        print(ss)
        
        # Save the user message
        q = "insert into chatai values(null,'%s','0','%s',curdate())" % (log_id, user_input)
        print(q, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
        insert(q)
        
        # Save the chatbot's response
        q = "insert into chatai values(null,'0','%s','%s',curdate())" % (log_id, ss)
        print(q, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
        insert(q)

        return redirect(url_for('api.user_chat_ai_bot',log_id=log_id))

    q = "select * from chatai where (reciver_id='0' and sender_id='%s') or (reciver_id='%s' and sender_id='0')" % (log_id, log_id)
    print("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", q)
    res = select(q)
    data['chatemp'] = res
    data['lid'] = log_id
    print("ooooooooooooooooo111111",data['lid'])
	

    return render_template('user_chat_ai_bot.html', data=data,log_id=log_id)



