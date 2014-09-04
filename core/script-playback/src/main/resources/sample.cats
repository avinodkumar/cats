press GUIDE
press 10, GUIDE

press GUIDE, MENU, UP, UP

press 10, 3, GUIDE, MENU 

//These will work based on switch statement.
press 0
press 1
pressHold GUIDE, 10
/*This won't work*/
press 2
tune 300
tune "400"
REBOOT()
captureScreen()
captureScreen("D:/images/sample.jpg")
log "Script execution completed."

log "logging mac: ${settop.getHostMacAddress()}"
log "logging manufacturer: ${settop.getManufacturer()}"

waitForFullImage("D://path")
waitForImageRegion("path")
waitForImageRegion("path", "region")