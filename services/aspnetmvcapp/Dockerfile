ARG interpolator_image=dockertemplate/interpolator:v0.1.8
FROM ${interpolator_image}
COPY assets .
CMD ["-skip-template", "*.eot,*.md"]