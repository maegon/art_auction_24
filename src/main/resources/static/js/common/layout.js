class App extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      number: 0
    }
  }
  componentDidMount() {
    
    let interval = setInterval(() => {
      const { number } = this.state
      if (number < 100) {
        this.setState({ number: number + 1})
      } else {
        interval.clearInterval()
      }
    }, 50)
  }
  render() {
    return (
      <div className="App">
        <Loader number={this.state.number} />
      </div>
    )
  }
}

const Loader = ({ number }) => {
  let numberString = number
  if(number < 10) {
    numberString = '0' + number
  }
  return (
    <div className="Loader" data-size={number}>{numberString}<sup>%</sup></div>
  )
}

ReactDOM.render(
  <App />,
  document.getElementById('root')
)