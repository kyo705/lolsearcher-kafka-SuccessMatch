package com.lolsearcher.persistence.successmatch.entity.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "MEMBERS",indexes = @Index(columnList = "summonerId"))
public class Member {
	
	@EmbeddedId
	private MemberCompKey ck;

	@JsonBackReference
	@MapsId("matchId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MATCH_ID", referencedColumnName = "ID")
	private Match match;

	@JsonManagedReference
	@OneToOne(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Perks perks;

	private Short assists;
	private Byte baronKills;
	private Byte bountyLevel;
	private int champExperience;
	private Byte champLevel;
	private Short championId;
	private String championName;
	private int championTransform;
	private int consumablesPurchased;
	private int damageDealtToBuildings;
	private int damageDealtToObjectives;
	private int damageDealtToTurrets;
	private int damageSelfMitigated;
	private Short deaths;
	private Short detectorWardsPlaced;
	private Short doubleKills;
	private Byte dragonKills;
	private boolean firstBloodAssist;
	private boolean firstBloodKill;
	private boolean firstTowerAssist;
	private boolean firstTowerKill;
	private boolean gameEndedInEarlySurrender;
	private boolean gameEndedInSurrender;
	private int goldEarned;
	private int goldSpent;
	private String individualPosition;
	private Byte inhibitorTakedowns;
	private Byte inhibitorsLost;
	private Short item0;
	private Short item1;
	private Short item2;
	private Short item3;
	private Short item4;
	private Short item5;
	private Short item6;
	private Short itemsPurchased;
	private int killingSprees;
	private Short kills;
	private String lane;
	private int largestCriticalStrike;
	private int largestKillingSpree;
	private int largestMultiKill;
	private int longestTimeSpentLiving;
	private int magicDamageDealt;
	private int magicDamageDealtToChampions;
	private int magicDamageTaken;
	private int neutralMinionsKilled;
	private Byte nexusKills;
	private Byte nexusTakedowns;
	private Byte nexusLost;
	private int objectivesStolen;
	private int objectivesStolenAssists;
	private int participantId;
	private Short pentaKills;
	private int physicalDamageDealt;
	private int physicalDamageDealtToChampions;
	private int physicalDamageTaken;
	private int profileIcon;
	private String puuid;
	private Short quadraKills;
	private String riotIdName;
	private String riotIdTagline;
	private String role;
	private Short sightWardsBoughtInGame;
	private Short spell1Casts;
	private Short spell2Casts;
	private Short spell3Casts;
	private Short spell4Casts;
	private Short summoner1Casts;
	private Short summoner1Id;
	private Short summoner2Casts;
	private Short summoner2Id;
	private String summonerId;
	private int summonerLevel;
	private String summonerName;
	private boolean teamEarlySurrendered;
	private Short teamId;
	private String teamPosition;
	private int timeCCingOthers;
	private int timePlayed;
	private int totalDamageDealt;
	private int totalDamageDealtToChampions;
	private int totalDamageShieldedOnTeammates;
	private int totalDamageTaken;
	private int totalHeal;
	private int totalHealsOnTeammates;
	private int totalMinionsKilled;
	private int totalTimeCCDealt;
	private int totalTimeSpentDead;
	private int totalUnitsHealed;
	private Short tripleKills;
	private int trueDamageDealt;
	private int trueDamageDealtToChampions;
	private int trueDamageTaken;
	private Byte turretKills;
	private Byte turretTakedowns;
	private Byte turretsLost;
	private Short unrealKills;
	private Short visionScore;
	private Short visionWardsBoughtInGame;
	private Short wardsKilled;
	private Short wardsPlaced;
	private boolean win;

	public void removeMatch() {
		if(match!=null) {
			match.removeMember(this);
			match = null;
		}
	}

	public void setMatch(Match match) {
		if(this.match!=null) {
			this.match.removeMember(this);
		}
		this.match = match;
		this.match.addMember(this);
	}
}
