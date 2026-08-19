import { useEffect, useRef } from 'react'
import {
    Viewer,
    Cartesian3,
    Color,
    VerticalOrigin,
    ScreenSpaceEventHandler,
    ScreenSpaceEventType,
} from 'cesium'
import 'cesium/Build/Cesium/Widgets/widgets.css'

function MissionMap() {
    const mapRef = useRef(null)

    useEffect(() => {
        const viewer = new Viewer(mapRef.current, {
            animation: false,
            timeline: false,
            baseLayerPicker: false,
            geocoder: false,
            homeButton: false,
            sceneModePicker: false,
            navigationHelpButton: false,
        })

        // AS-001 - Bengaluru
        viewer.entities.add({
            name: 'AS-001',
            position: Cartesian3.fromDegrees(77.5946, 12.9716),
            point: {
                pixelSize: 14,
                color: Color.GREEN,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-001',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        // AS-002 - Mysuru
        viewer.entities.add({
            name: 'AS-002',
            position: Cartesian3.fromDegrees(76.6394, 12.2958),
            point: {
                pixelSize: 14,
                color: Color.BLUE,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-002',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        // AS-003 - Tumakuru
        viewer.entities.add({
            name: 'AS-003',
            position: Cartesian3.fromDegrees(77.1010, 13.3392),
            point: {
                pixelSize: 14,
                color: Color.RED,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-003',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        const handler = new ScreenSpaceEventHandler(viewer.scene.canvas)

        handler.setInputAction((click) => {
            const pickedObject = viewer.scene.pick(click.position)

            if (pickedObject && pickedObject.id) {
                const drone = pickedObject.id

                alert(
                    `${drone.name}\nStatus: ${drone.properties?.status || 'Available'}`
                )
            }
        }, ScreenSpaceEventType.LEFT_CLICK)

        viewer.camera.flyTo({
            destination: Cartesian3.fromDegrees(
                77.0,
                12.8,
                150000
            ),
        })

        return () => {
            viewer.destroy()
        }
    }, [])

    return (
        <div
            ref={mapRef}
            style={{
                width: '100%',
                height: '480px',
            }}
        />
    )
}

export default MissionMap