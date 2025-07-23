FROM ubuntu

RUN apt update -y \
	&& apt install -y iputils-ping \
	&& rm -rf /var/lib/apt/lists/*
CMD ["sleep", "9999"]

