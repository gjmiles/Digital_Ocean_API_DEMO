import mysql.connector as mariadb
import os
from stat import *
from datetime import date
import glob
import re

def get_config():
    pass

def init_db():
    mariadb_conn = mariadb.connect(user='python_user', password='no_password', database='cs437project')
    cursor = mariadb_conn.cursor()
    cursor.execute("CREATE TABLE IF NOT EXISTS image_meta")
    return (mariadb_conn,cursor)

def get_meta_data(files_path,meta_data_list):
    #file_size = 0
    #file_name = ""
    #file_date = 0
    #compressed = False
    #meta_data = {'file_name':file_name,'file_date':file_date,'file_size':file_size,'compressed':compressed}

    all_image_files = glob.glob(files_path + "/*")
    #p = re.compile('.*\/(\w+\.\w+)\.?(\w+)?')

    for image_file_name in all_image_files:
        file_size = 0
        file_name = ""
        file_date = 0
        compressed = False
        meta_data = {'file_name':file_name,'file_date':file_date,'file_size':file_size,'compressed':compressed}
        #m = p.match(image_file_name)
        splits = image_file_name.split("/")
        print(splits[-1])
        meta_data['file_name'] = splits[-1]
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
        curs.execute("INSERT IGNORE INTO `image_meta` VALUES"  +
                     "(" + "\""+ meta_data["file_size"] + "\"" + "," + 
	             "\""+ meta_data["file_name"] + "\"" + "," +
                     "\""+ meta_data["file_date"] + "\"" + "," + "\"" + meta_data["compressed"] + "\")\"")
    
def main():
    file_path = "/home/ninelegs/CSULA_Classes/WINTER2016/CS496B/project/cs437/eclipse_workspace/download_manager/WebContent/geoimages"
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
    mariadb_conn.close()
    
if __name__ == "__main__":
    main()    


