This is a faux money sharing app created as one of my capstone's at Tech Elevator
It was pair programmed with my awesome partner Kurt

The server side
   *Creates account for new users and automatically loads it with $1000
   *Creates endpoints for HTTP requests for sending money, requesting money, approving/denying transactions
   *Also has endpoints for viewing usernames of the other users to send to
   *Validates user's identity using web tokens
   *Models accounts, transactions, and users to match database info
   *Uses DAO interfaces to create, read, update, and post info to the database

The client side
   *Not fully finished, but authorization is done
   *Will flush out once I learn more about Vue.js
