function addTablerow(contents) {
    let table = document.getElementsByClassName("flex-table")[0];
    let row = document.createElement('div');
    row.className = "table-row";
    let additionalclass = "";
    for(let i = 0; i < contents.length; i++) {
        if(i === 0 && contents[i] === "$hl") {
            row.className += " highlight";
        } else if(i === 0 && contents[i] === "$l") {
            row.className += " little"
        } else {
            let dom = document.createElement('div');
            dom.className = "table-row-item" + additionalclass;
            dom.innerText = contents[i];
            row.appendChild(dom);
        }
    }
    table.appendChild(row);
}

addTablerow(["$hl", "Player Iterator"]);
addTablerow(["PLAYERNAME","If the team allows friendly fire.","Boolean"]);
addTablerow(["PLAYERUUID","UUID of the player with dashes.","String"]);
addTablerow(["PLAYERDISPLAYNAME","Displayname of the player.","String"]);
addTablerow(["PLAYERTEAM","Scoreboard team of the player as JSON.","String#JSON"]);
addTablerow(["PLAYERPING","Ping of the player.","Int"]);
addTablerow(["PLAYERISLEGACY","Whether the player uses a legacy account.","Boolean"]);

addTablerow(["$hl","Teams Iterator"]);
addTablerow(["TEAMALLOWFRIENDLYFIRE","If the team allows friendly fire.","Boolean"]);
addTablerow(["TEAMCOLLISIONRULE","Collisionrule of the Team.","String#Enum"]);
addTablerow(["TEAMCOLOR","Color of the team.","String#Enum"]);
addTablerow(["TEAMDEATHMESSAGEVISIBILITY","Deathmessage visibility ruleing of the team.","String#Enum"]);
addTablerow(["TEAMDISPLAYNAME","Displayname of the team.","String"]);
addTablerow(["TEAMNAME","Name of the team.","String"]);
addTablerow(["TEAMNAMETAGVISIBILITY","Nametag visibility of the team.","String#Enum"]);
addTablerow(["TEAMSEEFRIENDLYINVISIBLES","Whether the team can see friendly invisibles.","String#Enum"]);
addTablerow(["TEAMPREFIX","Prefix of the team.","String"]);
addTablerow(["TEAMSUFFIX","Suffix of the team.","String"]);
addTablerow(["TEAMMEMBERS","Membernames of the team.","Collection#String"]);

addTablerow(["$hl","Objective Iterator"]);
addTablerow(["OBJECTIVECRITERIA","Criteria of the objective.","String#Enum"]);
addTablerow(["OBJECTIVEDISPLAYNAME","Displayname of the objective.","String"]);
addTablerow(["OBJECTIVENAME","Name of the objective.","String"]);
addTablerow(["OBJECTIVERENDERTYPE","Rendertype of the objective.","String#Enum"]);

addTablerow(["$hl","Score Iterator"]);
addTablerow(["SCOREOBJECTIVENAME","Name of the associated objective.","String"]);
addTablerow(["SCOREPLAYERNAME","Name of the owning player.","String"]);
addTablerow(["SCOREVALUE","Value of the score.","Int"]);
