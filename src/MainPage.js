import React from "react";
import { withRouter, Link } from "react-router-dom";
import { ListGroup } from "react-bootstrap";
import Lifehack from "./Lifehack";

class MainPage extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            lifehacks: []
        }
    }

    componentDidMount() {
        fetch("http://localhost:8080/api/lifehacks")
            .then(response => response.json())
            .then(data => {
                console.log(data);
                this.setState({
                    lifehacks: data
                });
            })
    }
    
    render() {
        return (
            <div>
                <ListGroup>
                    {this.state.lifehacks.map(lifehack =>
                        <Link to={`/lifehacks/${lifehack.id}`} key={lifehack.id}>
                            <ListGroup.Item key={lifehack.id} style={{marginBottom: "16px"}}><Lifehack data={lifehack} /></ListGroup.Item>
                        </Link>
                    )}
                </ListGroup>
            </div>
        );
    }
}

export default withRouter(MainPage);