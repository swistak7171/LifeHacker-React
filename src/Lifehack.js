import React from "react";
import StarRatingComponent from 'react-star-rating-component';

class Lifehack extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            lifehack: props.data
        }
    }

    render() {
        return (
            <div>
                <h1>{this.state.lifehack.content}</h1>
                <div>
                    <p>Created at {this.formatDateString(this.state.lifehack.createdAt)}</p>
                </div>
                <div>
                    <StarRatingComponent name="rate" value={this.state.lifehack.rating} />
                </div>
            </div>
        );
    }

    formatDateString(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString();
    }
}

export default Lifehack;