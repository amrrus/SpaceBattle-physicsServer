package physics_server.Server;

public class PlayerKernel {

	private Connection conn;
	private Integer playerId;
	private Integer lives;
	private Boolean shooting;
	private Integer shots;
	private float timeSinceLastShot;
	private float timeSinceLastShotRegeneration;
	private Boolean canShoot;
	
	public PlayerKernel(Connection conn,Integer playerId) {
		this.conn=conn;
		this.playerId = playerId;
		this.lives = Constants.INITIAL_PLAYER_LIVES;
		this.shooting=false;
		this.shots = Constants.MAX_NUMBER_SHOTS_STORED;
		this.canShoot = false;
		this.timeSinceLastShot=0;
		this.timeSinceLastShotRegeneration=0;
	}
	
	public void hadCollider() {
		this.lives--;
		if (this.lives <0) {
			this.conn.sendPlayerDeath(this.playerId);
		}else {
			this.conn.sendPlayerLives(this.playerId,this.lives);
		}
	}
	public void updateShots(float delta) {
		this.timeSinceLastShot+=delta;
		if(this.shots != Constants.MAX_NUMBER_SHOTS_STORED) {
			this.timeSinceLastShotRegeneration+=delta;
		}
		
		if (this.timeSinceLastShotRegeneration>Constants.TIME_SHOT_REGENERATION_INTERVAL 
				&& this.shots<Constants.MAX_NUMBER_SHOTS_STORED) {
			this.timeSinceLastShotRegeneration-=Constants.TIME_SHOT_REGENERATION_INTERVAL;
			this.shots++;
			if (this.shots == Constants.MAX_NUMBER_SHOTS_STORED) {
				this.timeSinceLastShotRegeneration = 0;
			}
			this.conn.sendUpdateShots(this.playerId,this.shots);
			System.out.println("Shots"+this.shots);
		}
		
		if (this.timeSinceLastShot > Constants.TIME_BETWEEN_SHOTS && this.shots>0){
			this.canShoot = true;
		}else {
			this.canShoot = false;
		}
		
	}
	public void shoot() {
		this.shots--;
		this.timeSinceLastShot=0;
		this.conn.sendUpdateShots(this.playerId,this.shots);
	}
	
	public void setShooting(Boolean shooting) {
		this.shooting = shooting;
	}
	public Boolean getShooting() {
		return this.shooting;
	}

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public Connection getConn() {
		return conn;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public Integer getShots() {
		return shots;
	}

	public void setShots(Integer shots) {
		this.shots = shots;
	}

	public float getTimeSinceLastShot() {
		return timeSinceLastShot;
	}

	public void setTimeSinceLastShot(float timeSinceLastShot) {
		this.timeSinceLastShot = timeSinceLastShot;
	}

	public Boolean getCanShoot() {
		return canShoot;
	}
	
	
}
