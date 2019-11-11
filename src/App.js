import React from "react";
import "./App.css";
import { BrowserRouter, Switch, Route, Link } from "react-router-dom";
import { Button } from "react-bootstrap";
import MainPage from "./MainPage";

class App extends React.Component {

    render() {
        return (
            <div>
                <BrowserRouter>
                    <div style={{marginBottom: "16px"}}>
                        <Link to="/">
                            <h1 style={{display: "inline"}}>LifeHacker</h1>
                        </Link>
                        <Link to="/new">
                            <Button variant="primary" style={{marginLeft: "16px"}}>New lifehack</Button>
                        </Link>
                    </div>
                    <div>
                        <Switch>
                            <Route exact path="/" component={MainPage}/>
                        </Switch>
                    </div>
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
