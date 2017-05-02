# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
- None yet


## [1.2.0] - 2017-05-02
### Added
- This changelog.

### Changed
- Made public `StateMachine` methods `synchronized`.

### Removed
- `StateMachine`'s default constructor and setters for its state graph and input adapter were removed. Those originally
existed to make the class more JavaBean-compliant and to simplify dependency injection. However, that no longer seems to
be necessary and allows for some situations that required additional validation work.
