//https://www.npmjs.com/package/mqtt#client
import mqtt from "mqtt";

const clientId = "mqttjs_" + Math.random().toString(16).substr(2, 8);

const client = mqtt.connect('mqtt://tic-tac-toe-mqtt.hidora.com', {
    port: 11168,
    clientId: clientId,
    clean: false,
    keepAlive: true,
    timeoutSeconds: 10
});

client.on("connect", function () {
    client.subscribe(
        "ttt/all_games",
        {
            qos: 0
        },
        function (err) {

        if(err) {
            console.log("MQTT Subscribtion failed with error: " + err);
        }
    });

    client.subscribe(
        "ttt/all_game_histories",
        {
            qos: 0
        },
        function (err) {

            if(err) {
                console.log("MQTT Subscribtion failed with error: " + err);
            }
        });

    client.subscribe(
        "ttt/new_game",
        {
            qos: 2
        },
        function (err) {

            if(err) {
                console.log("MQTT Subscribtion failed with error: " + err);
            }
        });

    client.subscribe(
        "ttt/game",
        {
            qos: 2
        },
        function (err) {

            if(err) {
                console.log("MQTT Subscribtion failed with error: " + err);
            }
        });
});

export default client;
