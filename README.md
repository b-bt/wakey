# Wakey
Wakey is an Android app created for an university Android course that uses Geofencing to let users know when they're about to arrive at a certain location. No more missing that bus stop because you were sleeping or distracted on you phone!

## Who is it for?
<!-- Quem vai usar e por que vai usar? -->
Wakey is main target is public transit users (with an Android phone). But anyone wanting to be notified when getting near a certain place can benefit from it.

## Why is this relevant?
<!-- Justificativa & concorrentes -->
"This has been done before" you might be saying, and yes, Google Maps has a similar feature built-in in their "Navigation" feature whenever the selected transportation method is Public transportation or Walking. But Google Maps is a bloated application, with too many features, not really friendly to lower-tier or old devices, and also not commonly used during daily commute on developing countries. Our goal is to create develop an application for those people, being a low-impact, and not requiring too many phone resources, since showing the way and help navigating the city is not our aim, but simply help people whom want to focus on other activities while commuting. 

## The App
<!-- Descrever features, telas e fluxo -->
- Creates alarms based on location
- The last used alarms presets are saved and displayed on the initial screen, for easier creation of future alarms.


- The initial screen is a overview of the map and the created alarms
  - Search box to enter an address for wich to create a new alarm
  - Shows the created alarms with its radius circle
  
- To create an alarm:
  - Searches for an address using Google Map's API
  - Can refine the address using the map's pin
  - Can fine tune the radius
  - Can give the alarm a name
  
  
The initial screen setup can be found [here](https://drive.google.com/open?id=1b0wnnucXItotYLRs1FfaldNsWLoAPXp_).

A video of the App in use can be found [here](https://drive.google.com/open?id=1Ygyt_NN2gqNvXA97Ek9Y2J3W_4GY0VQU).

The .apk file can be found [here](https://github.com/tbbruno/wakey/blob/master/Wakey_Debug.apk).

## The Team
<!-- Como vai ser a divisão do trabalho? -->
The team is composed by the undergrad students [@tbbruno](http://github.com/b-bt) and [@zpsmota](http://github.com/zpsmota) and the activities are divided as follows:

| Activity | Resposible(s) |
| --- | --- |
| Conception and prototyping | @b-bt & @zpsmota|
| Bussiness Rules | @b-bt & @zpsmota |
|  Screens design and implementation | @b-bt |
| Location services | @zpsmota |
