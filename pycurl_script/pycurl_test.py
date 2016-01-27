import pycurl

with open('test.png', 'wb') as f:
    c = pycurl.Curl()
    c.setopt(c.URL, 'http://localhost:8080/cs320gjmiles/lab4/GetImageServlet?name=bean.png')
    c.setopt(c.WRITEDATA,f)
    c.perform()
    c.close()
