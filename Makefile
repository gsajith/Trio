
install-third-party:
# Install the python modules
	find . -maxdepth 3 -name "setup.py" -execdir sh -c "python {} install" \;
