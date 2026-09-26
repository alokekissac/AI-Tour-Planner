from flask import *
from database import *

admin=Blueprint('admin',__name__)

@admin.route('/admin_home')
def admin_home():
    
    return render_template('admin _home.html')

@admin.route('/admin_view_user',methods=['get','post'])
def admin_view_user():
    data={}
    q="SELECT * FROM `customer`"
    res=select(q)
    data['view']=res
    return render_template('admin_view_user.html',data=data)

@admin.route('/admin_manage_place_category',methods=['get','post'])
def admin_manage_place_category():
    data={}
    if 'submit' in request.form:
        cat=request.form['cat']
        # print("________",cat)
        q1="SELECT * FROM `place_category` WHERE `category_name`='%s'"%(cat)
        res=select(q1)
        print("::::::",q1)
        if res:
            flash("category aleady Added")
        else:
            q="INSERT INTO `place_category` VALUES(NULL,'%s')"%(cat)
            insert(q)

    if 'action' in request.args:
        action=request.args['action']
        cat_id=request.args['cat_id']
    else:
        action=None

    if action=="delete":
        q="DELETE FROM `place_category` WHERE `category_id`='%s'"%(cat_id)
        delete(q)
        return redirect(url_for('admin.admin_manage_place_category'))

    if action=='update':
        q="SELECT * FROM `place_category` WHERE `category_id`='%s'"%(cat_id)
        data['up']=select(q)

    if 'update' in request.form:
        cat=request.form['cat']

        q="UPDATE `place_category` SET `category_name`='%s' WHERE `category_id`='%s'"%(cat,cat_id)
        update(q)
        return redirect(url_for('admin.admin_manage_place_category'))

    q="select * from place_category"
    res=select(q)
    data['view']=res
    return render_template('admin_manage_place_category.html',data=data)

@admin.route('/admin_view_tour_provider',methods=['get','post'])
def admin_view_tour_provider():
    data={}
    q="SELECT * FROM `login` INNER JOIN `tour_provider` USING(`login_id`)"
    res=select(q)
    data['view']=res

    if 'action' in request.args:
        action=request.args['action']
        log_id=request.args['log_id']
    else:
        action=None

    if action=='accept':
        q="UPDATE `login` set `user_type`='provider' WHERE `login_id`='%s'"%(log_id)
        print(q)
        update(q)
        return redirect(url_for('admin.admin_view_tour_provider'))

    if action=='reject':
        q="UPDATE `login` set `user_type`='rejected' WHERE `login_id`='%s'"%(log_id)
        print(q)
        update(q)
        return redirect(url_for('admin.admin_view_tour_provider'))



    # 
    return render_template('admin_view_tour_provider.html',data=data)

@admin.route('/admin_view_place',methods=['get','post'])
def admin_view_place():
    data={}
    
    q="SELECT * FROM `places` INNER JOIN `place_category` USING(`category_id`)"
    res=select(q)
    data['view']=res
    return render_template('admin_view_place.html',data=data)

@admin.route('/admin_complaints',methods=['get','post'])
def admin_complaints():
    data={}
    q="SELECT * FROM `complaint` INNER JOIN `customer` USING(`customer_id`)"
    res=select(q)
    data['view']=res
    j=0
    for i in range(1,len(res)+1):
        if 'submit' +str(i) in request.form:
            reply=request.form['reply'+str(i)]
            q="UPDATE `complaint` SET `reply`='%s' WHERE `complaint_id`='%s' "%(reply,res[j]['complaint_id'])
            update(q)
            flash('success')
            return redirect(url_for('admin.admin_complaints'))
        j=j+1
    return render_template('admin_complaints.html',data=data)


@admin.route('/admin_view_booking',methods=['get','post'])
def admin_view_booking():
    data={}
    # q="SELECT * FROM `packages` INNER JOIN `booking` USING(`package_id`) INNER JOIN `customer` USING(`customer_id`) WHERE login_id='%s'"%(session['pro_id'])
    q="SELECT * FROM `packages` INNER JOIN `booking` USING(`package_id`) INNER JOIN `customer` USING(`customer_id`)"
    print(q)
    res=select(q)
    data['view']=res
    return render_template('admin_view_booking.html',data=data)
@admin.route('/admin_view_rating',methods=['get','post'])
def admin_view_rating():
    data={}
    q=" SELECT * FROM `review_ratings` INNER JOIN `packages` USING(`package_id`) INNER JOIN `customer` ON `customer`.`customer_id`=`review_ratings`.`customer`"
    res=select(q)
    data['view']=res
    return render_template('admin_view_rating.html',data=data)


@admin.route('/admin_view_guid',methods=['get','post'])
def admin_view_guid():
    data={}
    q="SELECT * FROM `login` INNER JOIN `guid` USING(`login_id`) INNER JOIN `places` USING(`place_id`)"
    data['view']=select(q)
    if 'action' in request.args:
        action=request.args['action']
        l_id=request.args['l_id']
    else:
        action=None
    
    if action == 'accept':
        q="UPDATE `login` SET `user_type`='guid' WHERE `login_id`='%s'"%(l_id)
        update(q)
        return redirect(url_for('admin.admin_view_guid'))
    if action == 'reject':
        q="UPDATE `login` SET `user_type`='rejected' WHERE `login_id`='%s'"%(l_id)
        update(q)
        return redirect(url_for('admin.admin_view_guid'))
    return render_template('admin_view_guid.html',data=data)


@admin.route('/admin_view_packages',methods=['get','post'])
def admin_view_packages():
    data={}
    q="SELECT  * FROM `packages` INNER JOIN `places` ON `places`.`place_id`=`packages`.`places` INNER JOIN `tour_provider` USING(`provider_id`)"
    data['view']=select(q)
    if 'action' in request.args:
        action=request.args['action']
        p_id=request.args['p_id']
    else:
        action=None

    if action == 'accept':
        q="UPDATE `packages` SET `pack_status`='Public' WHERE `package_id`='%s'"%(p_id)
        update(q)
        return redirect(url_for('admin.admin_view_packages'))
    return render_template('admin_view_packages.html',data=data)


