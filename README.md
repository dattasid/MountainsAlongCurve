# MountainsAlongCurve

This projects allows noise, for example perlin noise, to be applied along a curve. The noise is aligned with the tangent to the curve at all times, making it seem like the noise is bending with the curve.

Here is an example of a heightmap, created by rendering ridged perlin noise along 2 different curves:
![Example](https://github.com/dattasid/MountainsAlongCurve/blob/master/examples/2curves.png)

Rendered in a 3D program or a game, the mountains might look like this:
![Example](https://github.com/dattasid/MountainsAlongCurve/blob/master/examples/im1.png)

Algorithm:

1. Take original bezier curve.
1. Calculate an offset bezier curve by calculating the average normal at each control point, and moving the control point a certain distance along that normal. Bezier curve exact offsets are very difficult to calculate. These offsets are very approximate and will give bad results for complex (self intersecting etc) curves. 
1. Also calculate the offset on the other side.
1. Take the original curve and one offset. Convert the space between two curves to a row of triangles by moving along both curves in small increments.
1. Calculate the texture coordonates (ux, uy) for each point for all triangles. ux=t, where t is the parameter value for that point on the curve. uy=0 if the point is on the original curve, uy=1 if the point is on the offset curve.
1. Render each triangle. For each point on the triangle, calculate the texture coordinates using barycentric coordinates and render the noise value.
1. Repeat the same for the other offset curve.

Another set of examples:

![Example](https://github.com/dattasid/MountainsAlongCurve/blob/master/examples/broad.png)
![Example](https://github.com/dattasid/MountainsAlongCurve/blob/master/examples/im3.png)
