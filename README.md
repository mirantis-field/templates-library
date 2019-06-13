# Templates Library

## Setup

### Add Repository

**Linux**: Open `~/.docker/application-template/preferences.yaml`

**Windows**: Open `%USERPROFILE%\.docker\application-template\preferences.yaml`

Add the repository as the first item in the repositories list:

```
repositories:
- name: Docker Field
  url: https://docker-field-application-template.s3-us-west-2.amazonaws.com/production/latest/library.yaml
```

The file should look something like this:

```
apiVersion: v1alpha1
disableFeedback: false
kind: Preferences
repositories:
- name: Docker Field
  url: https://docker-field-application-template.s3-us-west-2.amazonaws.com/production/latest/library.yaml
- name: library
  url: https://docker-application-template.s3.amazonaws.com/production/v0.1.5/library.yaml
```
