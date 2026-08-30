function DroneCard({ drone }) {
    return (
        <div className="drone-card">

            <div className="drone-header">
                <h3>{drone.name}</h3>

                <span className={`drone-status ${drone.status.toLowerCase()}`}>
                    {drone.status}
                </span>
            </div>

            <div className="drone-details">

                <div>
                    <span>Battery</span>
                    <strong>{drone.battery}%</strong>

                    <div className="battery-bar">
                        <div
                            className={`battery-level ${
                                drone.battery >= 70
                                    ? 'battery-good'
                                    : drone.battery >= 30
                                        ? 'battery-medium'
                                        : 'battery-low'
                            }`}
                            style={{ width: `${drone.battery}%` }}
                        ></div>
                    </div>
                </div>

                <div>
                    <span>Location</span>
                    <strong>{drone.location}</strong>
                </div>

            </div>

        </div>
    )
}

export default DroneCard