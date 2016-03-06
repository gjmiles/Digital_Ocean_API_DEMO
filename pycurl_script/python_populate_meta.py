import mysql.connector as mariadb
import os
from stat import *
from datetime import date
import glob
import re
import hashlib

def get_config():
    pass

def init_db():
    mariadb_conn = mariadb.connect(user='python_user', password='no_password', database='cs437project')
    cursor = mariadb_conn.cursor()
    cursor.execute("CREATE TABLE IF NOT EXISTS image_meta( FileId char(32) PRIMARY KEY, Filename varchar(32) NOT NULL, Filetype varchar(12) NOT NULL, Filesize int NOT NULL, DateAdded datetime NULL, Link varchar(2083) NOT NULL)")
    return (mariadb_conn,cursor)

def get_meta_data(files_path,meta_data_list):
    #file_size = 0
    #file_name = ""
    #file_date = 0
    #compressed = False
    #meta_data = {'file_name':file_name,'file_date':file_date,'file_size':file_size,'compressed':compressed}
    print files_path
    all_image_files = glob.glob(files_path + "/*")
    #p = re.compile('.*\/(\w+\.\w+)\.?(\w+)?')
    print all_image_files

    for image_file_name in all_image_files:
        file_size = 0
        file_name = ""
        file_date = 0
        file_id = ""
        file_type = ""
        compressed = False
        meta_data = {'file_id':file_id,'file_name':file_name,'file_date':file_date,'file_size':file_size,'compressed':compressed,'file_type':file_type}
        #m = p.match(image_file_name)
        splits = image_file_name.split("/")
        print(splits[-1])
        fullname = splits[-1]
        split_name = fullname.split(".",2)        
        with open(image_file_name,'r') as f:
            read_data = f.read()
            meta_data['file_id'] = hashlib.md5(read_data).hexdigest()
        meta_data['file_name'] = split_name[0]
        meta_data['file_type'] = split_name[1]
        #if m:
        #    print("rexeg match: ",m.group(1))
        if "xz" in splits[-1]:
                #print("rexeg match: ",m.group(2))            
                print("Compressed")
                compressed = True
                meta_data['compressed'] = True
        #print("File name: ", image_file_name)
        st = os.stat(image_file_name)
        change_date_time = date.fromtimestamp(st[ST_CTIME])
        print("File size (bytes): ",st[ST_SIZE])
        print("File C TIME: ",change_date_time.strftime('%Y-%m-%d %H:%M:%S'))
        meta_data['file_size'] = st[ST_SIZE]
        meta_data['file_date'] = change_date_time.strftime('%Y-%m-%d %H:%M:%S')
        meta_data_list.append(meta_data)

def conditional_insert(curs, meta_data_list):
    for meta_data in meta_data_list:
        insert_string = "INSERT IGNORE INTO `image_meta`(FileId, Filename, Filetype, Filesize, DateAdded) VALUES"  + \
        "(" + "\""+ meta_data["file_id"] + "\", " + \
        "\""+ meta_data["file_name"] + "\"" + ", " + \
        "\""+ meta_data["file_type"] + "\"" + ", " + \
        "\""+ str(meta_data["file_size"]) + "\"" + "," + "\"" + meta_data["file_date"] + "\")"
        print insert_string
        curs.execute(insert_string)
    
def main():
    file_path = "/home/ninelegs/CS437/cs437/do_servlet_controller/src/main/webapp/geoimages"
    meta_data_list = []
    get_meta_data(file_path,meta_data_list)
    print("PRINT DICT")
    for meta_data in meta_data_list:
        for k,v in meta_data.items():
            print("{} : {}".format(k,v))
    db_objects = init_db()
    mariadb_conn = db_objects[0]
    cursor = db_objects[1]
    conditional_insert(cursor,meta_data_list)
    mariadb_conn.commit()
    mariadb_conn.close()
    
if __name__ == "__main__":
    main()    


