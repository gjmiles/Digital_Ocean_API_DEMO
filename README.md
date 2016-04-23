#Download Manager Client

Basic download functionality, requests JSON metadata from a server,
then populates file listing based on metadata recieved from server.

Uses Digital Ocean API and requires Digital Ocean account.

Files can then be selected for download, part of metadata indicates location
of file.

##ISSUES / BUGS

- Need to be able to configure server(s) in client.
- Needs a major timer overhaul for getting JSON metadata,
  timer should be stopped on program exit.
- Program needs to not crash upon startup with a 500 HTTP error.
- Need to be able to choose a download directory.
- Need to indicate downloads finished and/or remove from the downloads view.
- Need to enable proper JSON API calls and include authorization keys.
- md5sum check on download file to indicate file integrity.
- Need to be able to pause downloads and resume.
